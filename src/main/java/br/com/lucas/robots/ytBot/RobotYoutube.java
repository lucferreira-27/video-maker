package br.com.lucas.robots.ytBot;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Thumbnails.Set;
import com.google.api.services.youtube.model.ThumbnailSetResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;

import br.com.lucas.application.Content;
import br.com.lucas.config.Configuration;
import br.com.lucas.robots.fileBot.RobotFile;
import br.com.lucas.robots.txtBot.Sentences;
import br.com.lucas.util.JavaHTTPServer;
import br.com.lucas.util.RWFilesUtil;
import express.Express;

public class RobotYoutube {
	private final JsonFactory JSON_FACTORY = new JacksonFactory();
	private final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private RobotFile botFile = new RobotFile();
	private YouTube youtube;
	private Video videoMetadata;
	private JavaHTTPServer server = new JavaHTTPServer();
	private GoogleClientSecrets cs;
	private GoogleAuthorizationCodeFlow flow;
	private Credential credential;
	private Express app;
	private Content content;
	private Configuration config;
	private RWFilesUtil rw = new RWFilesUtil();

	private final String folderResource = System.getProperty("user.dir") + "\\resourses\\";

	public RobotYoutube(Content content,Configuration config) {
		// TODO Auto-generated constructor stub
		this.content = content;
		this.config = config;
	}

	public void start() {
		authenticateWitchOAuth();
		uploadVideo();
		//uploadThumbnail();

	}

	private void authenticateWitchOAuth() {
		startWebServer();
		createOAuthClient();
		waitForGoogleCallBack();
		requestGoogleForAccessTokens(server.getCode(), cs);
		stopServer();

	}

	private void uploadVideo() {

		videoMetadata = new Video();
		VideoSnippet snippet = new VideoSnippet();
		snippet.setTitle(content.getPrefixText() + " " + content.getSearch());
		String descri = content.getListSentences().get(0).getText();
		snippet.setDescription(descri);
		List<String> tags = new ArrayList<>();

		content.getListSentences().stream().forEach(s -> tags.add(s.getKeywrods().get(0)));

		snippet.setTags(tags);
		VideoStatus status = new VideoStatus();
		status.setPrivacyStatus("unlisted");

		videoMetadata.setStatus(status);
		videoMetadata.setSnippet(snippet);
		File videoFile = new File(botFile.getFolderVideoOutput() + content.getPrefixText()
				+ content.getSearch() + ".mov");
		youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("youtube-upload")
				.build();

		try {
			InputStreamContent mediaContent = new InputStreamContent("video/*",
					new BufferedInputStream(new FileInputStream(videoFile)));
			mediaContent.setLength(videoFile.length());
			YouTube.Videos.Insert videoInsert = youtube.videos().insert(Arrays.asList("snippet,statistics,status"),
					videoMetadata, mediaContent);

			MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();
			uploader.setDirectUploadEnabled(false);

			MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
				public void progressChanged(MediaHttpUploader uploader) throws IOException {
					switch (uploader.getUploadState()) {
					case INITIATION_STARTED:
						System.out.println("Initiation Started");
						break;
					case INITIATION_COMPLETE:
						System.out.println("Initiation Completed");
						break;
					case MEDIA_IN_PROGRESS:
						System.out.println("Upload in progress");
						System.out.println("Upload percentage: " + Math.round(uploader.getProgress() * 100));
						break;
					case MEDIA_COMPLETE:
						System.out.println("Upload Completed!");
						break;
					case NOT_STARTED:
						System.out.println("Upload Not Started!");
						break;
					}
				}
			};
			uploader.setProgressListener(progressListener);
			Video returnedVideo = videoInsert.execute();

			System.out.println("\n================== Returned Video ==================\n");
			System.out.println("  - Id: " + returnedVideo.getId());
			System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
			System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
			System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
			System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void uploadThumbnail() {

		try {
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("youtube-upload")
					.build();
			String videoId = "Sa_szJJNrTE";// videoMetadata.getId();
			System.out.println("VideoId " + videoId);
			String thumbnailPath = folderResource + "\\images\\youtube-thumbnail\\youtube-thumbnail.jpg";
			File imageFile = new File(thumbnailPath);
			InputStreamContent mediaContent;
			mediaContent = new InputStreamContent("image/jpeg",
					new BufferedInputStream(new FileInputStream(imageFile)));
			mediaContent.setLength(imageFile.length());

			Set thumbnailSet = youtube.thumbnails().set(videoId, mediaContent);
			MediaHttpUploader uploader = thumbnailSet.getMediaHttpUploader();
			uploader.setDirectUploadEnabled(false);
			MediaHttpUploaderProgressListener progressListener = new MediaHttpUploaderProgressListener() {
				@Override
				public void progressChanged(MediaHttpUploader uploader) throws IOException {
					switch (uploader.getUploadState()) {
					// This value is set before the initiation request is
					// sent.
					case INITIATION_STARTED:
						System.out.println("Initiation Started");
						break;
					// This value is set after the initiation request
					// completes.
					case INITIATION_COMPLETE:
						System.out.println("Initiation Completed");
						break;
					// This value is set after a media file chunk is
					// uploaded.
					case MEDIA_IN_PROGRESS:
						System.out.println("Upload in progress");
						System.out.println("Upload percentage: " + uploader.getProgress());
						break;
					// This value is set after the entire media file has
					// been successfully uploaded.
					case MEDIA_COMPLETE:
						System.out.println("Upload Completed!");
						break;
					// This value indicates that the upload process has
					// not started yet.
					case NOT_STARTED:
						System.out.println("Upload Not Started!");
						break;
					}
				}
			};
			uploader.setProgressListener(progressListener);
			ThumbnailSetResponse setResponse = thumbnailSet.execute();
			System.out.println("\n================== Uploaded Thumbnail ==================\n");
			System.out.println("  - Url: " + setResponse.getItems().get(0).getDefault().getUrl());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void startWebServer() {

		server.start();
	}

	private void createOAuthClient() {
		try {
			JsonFactory JSON_FACTORY = new JacksonFactory();
			HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
			List<String> scopes = new ArrayList<String>();
			scopes.add("https://www.googleapis.com/auth/youtube");
			// InputStream resourceAsStream = new FileInputStream(folderResource +
			// "GOOGLE_APPLICATION_CREDENTIALS.json");
			// GoogleCredential credentials =
			// GoogleCredential.fromStream(resourceAsStream).createScoped(Lists.newArrayList("https://www.googleapis.com/auth/youtube"));

			cs = GoogleClientSecrets.load(JSON_FACTORY,
					new FileReader(botFile.getFolderCredencias() + "google_oauth2.json"));

//		    GoogleAuthorizationCodeRequestUrl authorizationCodeURL=new GoogleAuthorizationCodeRequestUrl(cs.getDetails().getClientId(), cs.getDetails().getRedirectUris().get(0), credentials.getServiceAccountScopes());
//
//		    authorizationCodeURL.setAccessType("offline");//For future compatibility
//
//		    String authorizationURL=authorizationCodeURL.build();
//		    System.out.println("AUTHORIZATION URL: "+authorizationURL); 

			flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
					(String) cs.getDetails().getClientId(), (String) cs.getDetails().getClientSecret(), scopes).build();
			GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();
			List<String> redirect_uris = (List<String>) cs.get("redirect_uris");
			url.setRedirectUri(cs.getDetails().getRedirectUris().get(0));
			url.setAccessType("offline");
			String authorize_url = url.build();
			System.out.println(authorize_url);

//
//			
//			credentials.refreshIfExpired();
//			AccessToken token = credentials.getAccessToken();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void waitForGoogleCallBack() {
		synchronized (server) {
			try {
				server.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void requestGoogleForAccessTokens(String token, GoogleClientSecrets cs) {

//		AccessToken accessToken = new AccessToken("1/MkSJoj1xsli0AccessToken_NKPY2", null);
//		 OAuth2Credentials credentials = OAuth2Credentials.newBuilder()
//		   .setAccessToken(accessToken)
//		   .build();
		try {

			GoogleTokenResponse response = new GoogleAuthorizationCodeTokenRequest(new NetHttpTransport(),
					new JacksonFactory(), cs.getDetails().getClientId(), cs.getDetails().getClientSecret(), token,
					cs.getDetails().getRedirectUris().get(0)).execute();
			credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
					.setAccessToken(response.getAccessToken());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void stopServer() {
		server.closeServer();
	}

}
