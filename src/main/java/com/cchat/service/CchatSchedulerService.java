package com.cchat.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cchat.constants.MessageCodes;
import com.cchat.models.CommonExample;
import com.cchat.models.Data;
import com.cchat.models.Message;
import com.cchat.models.RasaNluData;
import com.cchat.models.TrainRequest;

@Service
public class CchatSchedulerService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CchatSchedulerService.class);

	public TrainRequest getTrainRequestInstance(List<CommonExample> commonExamplesList, String NLU_LANGUAGE,
			String ML_PIPELINE) {
		TrainRequest trainRequest = new TrainRequest();
		trainRequest.setLanguage(NLU_LANGUAGE);
		trainRequest.setPipeline(ML_PIPELINE);
		Data data = new Data();
		RasaNluData rasaNluData = new RasaNluData();
		rasaNluData.setCommonExamples(commonExamplesList);
		data.setRasaNluData(rasaNluData);
		trainRequest.setData(data);
		return trainRequest;
	}

	public String trainNLU(String mergedTrainingDataJson, String NLU_URL, String PROJECT_NAME, String MODEL_NAME) {
		LOGGER.info("Training NLU");
		RestTemplate restTemplate = new RestTemplate();
		String url = NLU_URL + "/train?project=" + PROJECT_NAME + "&model=" + MODEL_NAME;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(mergedTrainingDataJson, headers);
		return restTemplate.postForObject(url, entity, String.class);
	}

	public Message readExistingModelFromNlu() {
		LOGGER.info("Reading existing nlu model");
		StringBuilder data = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader("/app/test.json"))) {
			String line;
			while ((line = br.readLine()) != null) {
				data.append(System.lineSeparator() + line);
			}
			LOGGER.info("Existing model json =  " + data);
		} catch (Exception e) {
			Message message = new Message();
			message.setException(e.getStackTrace().toString());
			message.setCode(MessageCodes.READ_EXISTING_TRAINING_DATA_FAILED);
			message.setMessage(e.getMessage());
			message.setFlag(false);
			LOGGER.error(e.getMessage());
			return message;
		}
		Message message = new Message();
		message.setFlag(true);
		message.setMessage(data.toString());
		message.setCode(MessageCodes.SUCCESS);
		return message;
	}

	public Message commitLatestTrainedDataToGitLab(String backupNewModelJson, String GIT_USERNAME, String GIT_PASSWORD,
			String GIT_URL, String REPO_NAME) {
		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(
				GIT_USERNAME, GIT_PASSWORD);

		File file = new File("/tmp/test/" + REPO_NAME);
		// By passing SSL Verification before cloning repo
		byPassSSLVerification();

		try (Git git = Git.cloneRepository().setURI(GIT_URL).setCredentialsProvider(usernamePasswordCredentialsProvider)
				.setDirectory(file).call()) {

			Repository repository = git.getRepository();
			LOGGER.info("current branch name :=  " + repository.getBranch());
			LOGGER.info("current directory = " + repository.getDirectory().getParent());
			// create the folder
			File theDir = new File(repository.getDirectory().getParent(), "store");
			if (!theDir.exists()) {
				theDir.mkdir();
				LOGGER.info("New folder created ");
			}

			// Stage all files in the repo including new files
			git.add().addFilepattern(".").call();

			// and then commit the changes.
			git.commit().setMessage("Commit all changes including additions").call();

			try (FileWriter fileWriter = new FileWriter(new File(theDir, "nlu_training_data_backup.json"))) {
				fileWriter.write(backupNewModelJson);
				fileWriter.flush();
			} catch (Exception e) {
				Message message = new Message();
				message.setException(e.getStackTrace().toString());
				message.setCode(MessageCodes.FILE_CREATION_FAILED);
				message.setMessage(e.getMessage());
				message.setFlag(false);
				LOGGER.error(e.getMessage());
				return message;
			}
			git.add().addFilepattern(".").call();
			// Stage all changed files, omitting new files, and commit with one command
			git.commit().setAll(true).setMessage("stored backup at " + LocalTime.now()).call();
			// now open the created repository
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			try (Repository repository1 = builder.setGitDir(file).readEnvironment().findGitDir().build()) {
				LOGGER.info("build done !!!");
				LOGGER.info("pushing code to " + GIT_URL);
				git.push().setCredentialsProvider(usernamePasswordCredentialsProvider).call();
				LOGGER.info("Code Pushed successfully from repository: " + repository1.getDirectory()
						+ " to remote repository at " + GIT_URL);

				// delete existing folder in /tmp localtion
				if (file.exists()) {
					file.delete();
					LOGGER.info("Temporary file deleted successfully !!");
				}
			}
		} catch (Exception e) {
			Message message = new Message();
			message.setException(e.getStackTrace().toString());
			message.setCode(MessageCodes.GIT_PUSH_FAILED);
			message.setMessage("NLU successfully trained but latest training data is not pushed to GIT");
			message.setFlag(false);
			LOGGER.error(e.getMessage());
			return message;
		}
		Message message = new Message();
		message.setFlag(true);
		return message;
	}

	private void byPassSSLVerification() {
		File file = new File(System.getProperty("user.home") + "/.gitconfig");
		if (!file.exists()) {
			PrintWriter writer;
			try {
				writer = new PrintWriter(file);
				writer.println("[http]");
				writer.println("sslverify = false");
				writer.close();
			} catch (FileNotFoundException e) {
				LOGGER.error(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
