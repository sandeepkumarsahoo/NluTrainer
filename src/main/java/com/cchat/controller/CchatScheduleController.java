package com.cchat.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cchat.constants.MessageCodes;
import com.cchat.models.CommonExample;
import com.cchat.models.Message;
import com.cchat.models.TrainRequest;
import com.cchat.models.existing.ExistingModelRoot;
import com.cchat.service.CchatSchedulerService;
import com.cchat.utils.JsonUtility;

@RestController
public class CchatScheduleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CchatScheduleController.class);

	@Value("${nlu_language}")
	private String NLU_LANGUAGE;
	@Value("${ml_pipeline}")
	private String ML_PIPELINE;
	@Value("${project_name}")
	private String PROJECT_NAME;
	@Value("${model_name}")
	private String MODEL_NAME;
	@Value("${git_url}")
	private String GIT_URL;
	@Value("${git_username}")
	private String GIT_USERNAME;
	@Value("${git_password}")
	private String GIT_PASSWORD;
	@Value("${git_branch}")
	private String GIT_BRANCH;
	@Value("${nlu_url}")
	private String NLU_URL;
	@Value("${git_repo_name}")
	private String REPO_NAME;

	// @Autowired
	// @Qualifier("cchatSchedulerService")
	private CchatSchedulerService cchatSchedulerService = new CchatSchedulerService();;

	// @Scheduled(cron = "${CRON_EXPRESSION}")

	@PostMapping(path = "/nlu/train", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Message> trainNlu(@RequestBody List<CommonExample> commonExamplesList) {
		LOGGER.info("In controller ");

		if (commonExamplesList.size() == 0) {
			return new ResponseEntity<>(
					new Message("Expecting some training data to train NLU !!!", "200", "List size is 0!!!"),
					HttpStatus.OK);
		}

		// step 0 - Create a TrainRequest class instance and set the new request
		TrainRequest trainRequest = cchatSchedulerService.getTrainRequestInstance(commonExamplesList, NLU_LANGUAGE,
				ML_PIPELINE);
		LOGGER.info("parsed new trained data  " + JsonUtility.toJson(trainRequest));

		// step 1 Reading existing model
		Message message = cchatSchedulerService.readExistingModelFromNlu();
		if (!message.isFlag()) {
			return new ResponseEntity<>(message, HttpStatus.EXPECTATION_FAILED);
		}

		ExistingModelRoot existingModelRoot = JsonUtility.fromJson(message.getMessage(), ExistingModelRoot.class);
		LOGGER.info("parsed existing trained data  " + JsonUtility.toJson(existingModelRoot));

		// step-2 append old model to new model
		trainRequest.getData().getRasaNluData().getCommonExamples()
				.addAll(existingModelRoot.getRasaNluDataExisting().getCommonExampleExistings());
		String mergedTrainingDataInJson = JsonUtility.toJson(trainRequest);
		LOGGER.info("After merging :=  " + mergedTrainingDataInJson);

		// step-3 train NLU with latest data
		try {
			String messageFromNlu = cchatSchedulerService.trainNLU(mergedTrainingDataInJson, NLU_URL, PROJECT_NAME,
					MODEL_NAME);
			LOGGER.info("Response from NLU : " + messageFromNlu);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			Message m = new Message();
			m.setException(e.getMessage());
			m.setCode(MessageCodes.NLU_TRAIN_FAILED);
			m.setFlag(false);
			m.setMessage("Either request data format is not proper or NLU server is down !!");
			return new ResponseEntity<>(m, HttpStatus.EXPECTATION_FAILED);
		}
		// step 5 push to git
		Message gitMessage = cchatSchedulerService.commitLatestTrainedDataToGitLab(mergedTrainingDataInJson,
				GIT_USERNAME, GIT_PASSWORD, GIT_URL, REPO_NAME);
		if (!gitMessage.isFlag()) {
			return new ResponseEntity<>(gitMessage, HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>(new Message("NLU successfully trained.", MessageCodes.SUCCESS, "No exception."),
				HttpStatus.CREATED);
	}

}
