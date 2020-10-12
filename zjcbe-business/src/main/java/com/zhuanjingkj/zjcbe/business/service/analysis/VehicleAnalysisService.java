package com.zhuanjingkj.zjcbe.business.service.analysis;

import com.zhuanjingkj.zjcbe.business.dto.analysis.*;
import com.zhuanjingkj.zjcbe.business.dto.analysis.*;
import com.zhuanjingkj.zjcbe.business.dto.analysis.*;
import com.zhuanjingkj.zjcbe.business.service.analysis.analysishandle.AnalysisFileUploadService;
import com.zhuanjingkj.zjcbe.commondata.baseDTO.ResultDTO;
import com.zhuanjingkj.zjcbe.commondata.baseDTO.SimpleResultDTO;
import com.zhuanjingkj.zjcbe.commondata.config.CommonConfig;
import com.zhuanjingkj.zjcbe.commondata.enums.VehicleResourceEnum;
import com.zhuanjingkj.zjcbe.commondata.vehicleDTO.VehicleIdentifyMessageDTO;
import com.zhuanjingkj.zjcbe.commondata.vehicleDTO.VehicleImageMessageDTO;
import com.zhuanjingkj.zjcbe.domain.entity.ImageRectifyEntity;
import com.zhuanjingkj.zjcbe.domain.entity.VehicleRtmpShotEntity;
import com.zhuanjingkj.zjcbe.domain.entity.VehicleRtspconfigEntity;
import com.zhuanjingkj.zjcbe.domain.po.VehicleAnalysisPO;
import com.zhuanjingkj.zjcbe.domain.po.VehicleAnalysisWztzPO;
import com.zhuanjingkj.zjcbe.domain.po.VehicleImagesPO;
import com.zhuanjingkj.zjcbe.utility.image.ImageUtils;
import com.zhuanjingkj.zjcbe.utility.map.ObjectMapperUtils;
import com.zhuanjingkj.zjcbe.utility.output.CustomOutputUtility;
import com.zhuanjingkj.zjcbe.repository.analysis.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class VehicleAnalysisService implements IVehicleAnalysisService {

	private static Logger logger = LoggerFactory.getLogger(VehicleAnalysisService.class);

	@Autowired
	VehicleImagesRepository vehicleImagesRepository;

	@Autowired
	ImageRectifyRepository imageRectifyRepository;

	@Autowired
	VehicleAnalysisRepository analysisRepository;

	@Autowired
	VehicleAnalysisWztzRepository vehicleAnalysisWztzRepository;

	@Autowired
	AnalysisFileUploadService fileUploadService;

	@Autowired
	VehicleRtspConfigRepository rtspConfigRepository;

	@Autowired
	VehicleRtmpShotRepository shotRepository;

	@Autowired
	CommonConfig autoConfig;

	@Override
	public ResultDTO<VehicleAnalysisResponseDTO> imageAnalysis(MultipartFile file, boolean isDemo) {
		return fileUploadService.imageAnalysis(file, isDemo);
	}

	@Override
	public SimpleResultDTO saveRtmpAnalysisMessage(VehicleIdentifyMessageDTO responseDTO) {
		return fileUploadService.rtmpAnalysis(responseDTO);
	}

	@Override
	public SimpleResultDTO saveRtmpImageMessage(VehicleImageMessageDTO imageMessageDTO) {
		try {
			String suffixName = ".jpg";
			VehicleRtmpShotEntity shotEntity = new VehicleRtmpShotEntity();
			shotEntity.setStreamId(Integer.parseInt(imageMessageDTO.getStreamId()));
			shotEntity.setTimeStamp(Integer.parseInt(imageMessageDTO.getTimeStamp()));
			shotEntity.setSxh(imageMessageDTO.getSxh());

			VehicleResourceDTO resourceDTO = fileUploadService.getVehicleResourceByType(VehicleResourceEnum.SHOT, ".jpg");
			logger.info("### getResoucePath=" + resourceDTO.getResoucePath() + "!");
			ImageUtils.getBase64Image(imageMessageDTO.getImageByteStr(), resourceDTO.getResoucePath());
			shotEntity.setImagePath(resourceDTO.getResoucePath());
			shotEntity.setImageUrl(resourceDTO.getResouceUrl());
			shotEntity.setImageType(suffixName);
			shotEntity.setCreatetime(new Date());
			shotRepository.add(shotEntity);
			return CustomOutputUtility.excuteSuccess();
		} catch (Exception ex) {
			logger.error(String.format("处理图片消息出错，原因：%s", ex.getMessage(), ex));
			return CustomOutputUtility.excuteFail(ex.getMessage());
		}
	}

	@Override
	public ResultDTO<VideoUploadResponseDTO> videoUpload(MultipartFile file) {
		return fileUploadService.videoUpload(file);
	}

	@Override
	public ResultDTO<VehicleAnalysisResponseDTO> getAnalysisResult(String rstpId) {
		VehicleImagesPO imagesPO = vehicleImagesRepository.getLastVehicleImage(rstpId);
		if (imagesPO == null) {
			return CustomOutputUtility.excuteFail("未获取到最新解析数据");
		}
		VehicleAnalysisResponseDTO resultDTO = new VehicleAnalysisResponseDTO();
		ObjectMapperUtils.map(imagesPO, resultDTO);
		List<VehicleAnalysisPO> analysisPOS = analysisRepository.getAnalysisListByImageId(imagesPO.getImageId());
		if (analysisPOS != null) {
			resultDTO.setVeh(new ArrayList<>());
			for (VehicleAnalysisPO po : analysisPOS
			) {
				VehicleAnalysisDetailDTO detailDTO = new VehicleAnalysisDetailDTO();
				detailDTO.setSxh(po.getSxh());
				if (po.getSxh() == imagesPO.getSxh()) {
					System.out.println("##### imagesPO.getThumbImage()=" + imagesPO.getThumbImage() + "!");
					detailDTO.setThumbImage(imagesPO.getThumbImage());
				}
				detailDTO.setCxtz(new VehicleAnalysisCxtzDTO());
				ObjectMapperUtils.map(po, detailDTO.getCxtz());

				detailDTO.setGxhtz(new VehicleAnalysisGxhtzDTO());
				ObjectMapperUtils.map(po, detailDTO.getGxhtz());

				detailDTO.setHptz(new VehicleAnalysisHptzDTO());
				ObjectMapperUtils.map(po, detailDTO.getHptz());

				detailDTO.setJsxztz(new VehicleAnalysisJsxwtzDTO());
				ObjectMapperUtils.map(po, detailDTO.getJsxztz());

				detailDTO.setWztz(new VehicleAnalysisWztzDTO());
				ObjectMapperUtils.map(po, detailDTO.getWztz());

				fileUploadService.initAnalysisResponseDTO(detailDTO);
				resultDTO.getVeh().add(detailDTO);
			}
		}
		return CustomOutputUtility.excuteSuccess(resultDTO);
	}

	@Override
	public ResultDTO<RtmpConfigResponseDTO> getRtmpConfigByUrl(String rtmpUrl) {
		RtmpConfigResponseDTO rtmpConfigResponseDTO = new RtmpConfigResponseDTO();
		System.out.println("VehicleAnalysisService.getRtmpConfigByUrl: " + rtmpUrl + "!");
		VehicleRtspconfigEntity rtspconfigEntity = rtspConfigRepository.getByRtmpUrl(rtmpUrl);
		logger.info("rtspconfigEntity=" + rtspconfigEntity + "!");
		ObjectMapperUtils.map(rtspconfigEntity, rtmpConfigResponseDTO);
		return CustomOutputUtility.excuteSuccess(rtmpConfigResponseDTO);
	}

	@Override
	public ResultDTO<RectifyImgRecgRstDTO> rectifyImage(String imageId, String userJson, String userNotes) {
		RectifyImgRecgRstDTO dto = new RectifyImgRecgRstDTO();
		dto.setImageId(imageId + "_yt");
		ImageRectifyEntity entity = new ImageRectifyEntity();
		entity.setImageId(imageId);
		entity.setImageState(2);
		entity.setUserJson(userJson);
		if (imageRectifyRepository.add(entity)) {
			return CustomOutputUtility.excuteSuccess(dto);
		} else {
			return CustomOutputUtility.excuteFail("添加图片纠错内容失败");
		}
	}

	@Override
	public ResultDTO<VehicleAnalysisResponseDTO> getImageRecgJson(String imageId) {
		VehicleAnalysisResponseDTO dto = new VehicleAnalysisResponseDTO();
		VehicleImagesPO vehicleImagesPO = vehicleImagesRepository.getImageInfo(imageId);
		dto.setImageId(imageId);
		dto.setImageUrl(vehicleImagesPO.getImageUrl());
		dto.setImageName(vehicleImagesPO.getImageName());
		List<VehicleAnalysisDetailDTO> detailDTOS = new ArrayList<>();
		VehicleAnalysisDetailDTO vadDTO = null;
		List<VehicleAnalysisPO> vpos = analysisRepository.getAnalysisListByImageId(imageId);
		String analysisId = null;
		VehicleAnalysisWztzDTO vaWztzDTO = null;
		for (VehicleAnalysisPO vpo : vpos) {
			analysisId = vpo.getAnalysisId();
			vadDTO = new VehicleAnalysisDetailDTO();
			vadDTO.setSxh(vpo.getSxh());
			vadDTO.setWztz(getWztzDTO(analysisId));
			logger.info("########### analysisId: " + analysisId + "!");
			detailDTOS.add(vadDTO);
		}
		dto.setVeh(detailDTOS);
		return CustomOutputUtility.excuteSuccess(dto);
	}

	/**
	 * 获取位置特征
	 * @param analysisId
	 * @return
	 */
	private VehicleAnalysisWztzDTO getWztzDTO(String analysisId) {
		VehicleAnalysisWztzDTO vaWztzDTO = new VehicleAnalysisWztzDTO();
		VehicleAnalysisWztzPO wztz = vehicleAnalysisWztzRepository.getVehicleAnalysisWztz(analysisId);
		vaWztzDTO.setClwz(wztz.getClwz());
		vaWztzDTO.setPsfx(wztz.getPsfx());
		return vaWztzDTO;
	}

	private VehicleAnalysisHptzDTO getHptzDTO(String analysisId) {
		VehicleAnalysisHptzDTO vaHptzDTO = new VehicleAnalysisHptzDTO();
		VehicleAnalysisHptzPO
		return vaHptzDTO;
	}
}
