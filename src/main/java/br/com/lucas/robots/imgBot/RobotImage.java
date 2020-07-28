package br.com.lucas.robots.imgBot;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlDivision;

import br.com.lucas.applicaiton.Content;
import br.com.lucas.config.Configuration;
import br.com.lucas.config.GoogleImagesConfig;
import br.com.lucas.robots.txtBot.Sentences;
import br.com.lucas.util.ConnectService;
import br.com.lucas.util.RWFilesUtil;
import br.com.lucas.util.RobotImageUtil;

public class RobotImage {
	private Content content;
	private Configuration config;
	private RWFilesUtil rw = new RWFilesUtil();
	private List<String> downloadImages = new ArrayList<>();
	private String folderDest = System.getProperty("user.dir") + "\\resourses\\images\\";

	public RobotImage(Content content, Configuration config) {
		// TODO Auto-generated constructor stub
		this.content = content;
		this.config = config;
	}

	public void start() {
		feachImagesOfAllSentences(content.getListSentences());
		rw.saveJson(content);

		downlaodAllImages(content.getListSentences(), content.getThumbnailSentence());

	}

	private void feachImagesOfAllSentences(List<Sentences> sentences) {
		for (Sentences s : sentences) {
			String keyword = s.getKeywrods().get(0);
			if (content.getSearch().equals(keyword))
				keyword = s.getKeywrods().get(1);
			System.out.println("> [Searching] - " + content.getSearch() + " " + s.getKeywrods().get(0));
			featchGoogleAndReturnImagesLinks(content.getSearch() + keyword, s, null);

		}
		featchGoogleAndReturnImagesLinks(content.getSearch(), null, content.getThumbnailSentence());

	}

	private void downlaodAllImages(List<Sentences> sentences, Sentences thumbnail) {
		int index = 0;
		String destOriginal = "\\imagens-original\\";

		for (Sentences s : sentences) {

			for (int i = 0; i < s.getImages().size(); i++) {
				try {
					System.out.println("> [Working] ...");

					downloadImage(s.getImages().get(i), folderDest + destOriginal, index);
					System.out.println("> [Sucess] Image: " + s.getImages().get(i));
					System.out.println("> [Save] in " + folderDest + destOriginal);
					index++;
					break;
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
				System.out.println("> Erro Image: " + s.getImages().get(i));

				System.out.println("> Trying to download another image");
			}
		}
		System.out.println("> Download Thumbnail");
		for (int i = 0; i < thumbnail.getImageThumbnail().size(); i++) {
			try {
				System.out.println("> [Working] ...");

				downloadImage(thumbnail.getImageThumbnail().get(i), folderDest + destOriginal, -1);
				System.out.println("> [Sucess] Image: " + thumbnail.getImageThumbnail().get(i));
				System.out.println("> [Save] in " + folderDest + destOriginal);
				index++;
				break;
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			System.out.println("> Erro Image: " + thumbnail.getImages().get(i));

			System.out.println("> Trying to download another image");
		}

	}

	private void downloadImage(String img, String dest, int index) throws Exception {
		if (!downloadImages.contains(img)) {
			try {
				URL url = new URL(img);
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				dest += index + "-original.png";
				httpConn.setRequestMethod("GET");
				httpConn.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0");

				// String fileName = url.getFile();

				// String destName = dest + "\\" + fileName;

				InputStream is = httpConn.getInputStream();
				OutputStream os = new FileOutputStream(dest);

				byte[] b = new byte[2048];
				int length;

				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}

				is.close();
				os.close();
				if(RobotImageUtil.imageIsCorruped(dest)) {
					throw new Exception("Imagem Corrompida");
				}
				downloadImages.add(img);
				
			} catch (Exception e) {
				// TODO: handle exception
				throw new Exception(e.getMessage());

			}
		} else
			throw new Exception("Imagem já baixada");
	}

	private void featchGoogleAndReturnImagesLinks(String querry, Sentences sentence, Sentences thumbnail) {
		String dataId = null;
		try {
			GoogleImagesConfig imagesConfig = config.getImagesConfig();
			imagesConfig.setQuerry(querry);
			imagesConfig.setDefineSize("m");
			
			
			if (sentence == null && thumbnail == null)
				throw new NullPointerException("Sentence and Thumbnail cant not be null");
			if (sentence != null && thumbnail != null)
				throw new NullPointerException("Sentence or Thumbnail need be null");
			if (thumbnail != null) {
				querry = querry.split(" ")[0];
				System.out.println(querry);
			}
			ConnectService service = new ConnectService(imagesConfig.encode());
			List<String> links = new ArrayList<>();
			List<HtmlDivision> imgDiv = service.getPage().getByXPath("//div[@class='isv-r PNCib MSM1fd BUooTd']");
			for (HtmlDivision div : imgDiv) {
				dataId = div.getAttribute("data-id");
				for (int i = 0; i < service.getPage().getElementsByTagName("script").getLength(); i++) {
					if (service.getPage().getElementsByTagName("script").get(i).getTextContent()
							.contains("AF_initDataCallback({key: 'ds:1', isError:  false , hash: '2', data:")) {
						try {
						String script = service.getPage().getElementsByTagName("script").get(i).getTextContent();
						script = script.substring(script.indexOf(dataId));
						script = script.substring(script.indexOf("]") + 1);
						script = script.substring(0, script.indexOf("]"));
						script = script.substring(script.indexOf("\"") + 1, script.lastIndexOf("\""));
						links.add(script);
						}catch (StringIndexOutOfBoundsException e) {
							// TODO: handle exception
							continue;
						}
					}
				}
			}
			if (thumbnail != null)
				thumbnail.setImageThumbnail(links);
			else
				sentence.setImages(links);
		} catch (ConnectException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(dataId);

			System.exit(0);
		}
// -- OLD METHOD
//		Builder builder = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(),
//		new JacksonFactory(), null);
//builder.setApplicationName("Search Test");
//builder.setCustomsearchRequestInitializer(new CustomsearchRequestInitializer() {
//	@Override
//	protected void initializeCustomsearchRequest(CustomsearchRequest<?> request) throws IOException {
//
//		request.setKey({apikey});
//		request.set("cx", "002538072246495558349:f50zjaf1kdg");
//		request.set("num", 2);
//		request.set("q", querry);
//		request.set("searchType", "image");
//		//request.set("imgSize", "large");
//	}
//});
//Customsearch customsearch = builder.build();
//Search searchResult = customsearch.cse().list().execute();
//List<String> links = new ArrayList<>();
//if (searchResult.getItems() == null)
//	return -1;
//for (int i = 0; i < searchResult.getItems().size(); i++) {
//	JSONObject jsonParser = new JSONObject(searchResult.getItems().get(i));
//	links.add(jsonParser.getString("link"));
//}
	}

}
