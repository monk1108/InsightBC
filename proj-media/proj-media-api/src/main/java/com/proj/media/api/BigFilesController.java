package com.proj.media.api;

import com.proj.base.model.RestResponse;
import com.proj.media.model.dto.UploadFileParamsDto;
import com.proj.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Description: upload interface for large files
 * @Author: Yinuo
 * @Date: 2023/10/19 14:04
 */
@Api(value = "Upload Interface for Big Files", tags = "Upload Interface for Big Files")
@RestController
public class BigFilesController {

    @Autowired
    MediaFileService mediaFileService;

    @ApiOperation(value = "check file before upload")
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkfile(
            @RequestParam("fileMd5") String fileMd5
    ) throws Exception {
        return mediaFileService.checkFile(fileMd5);
    }


    @ApiOperation(value = "check chunk before upload")
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkchunk(@RequestParam("fileMd5") String fileMd5,
                                            @RequestParam("chunk") int chunk) throws Exception {
        return mediaFileService.checkChunk(fileMd5, chunk);
    }

    @ApiOperation(value = "upload chunk file")
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadchunk(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("chunk") int chunk) throws Exception {
//        create a temp file to get local file path
        File tempFile = File.createTempFile("minio", "temp");
//        copy the file to temp file
        file.transferTo(tempFile);
        String absolutePath = tempFile.getAbsolutePath();
        return mediaFileService.uploadChunk(fileMd5, chunk, absolutePath);

    }

    @ApiOperation(value = "merge chunks")
    @PostMapping("/upload/mergechunks")
    public RestResponse mergechunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") int chunkTotal) throws Exception {
        Long companyId = 1232141425L;
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFileType("001002");
        uploadFileParamsDto.setTags("Course Video");
        uploadFileParamsDto.setRemark("");
        uploadFileParamsDto.setFilename(fileName);

        return mediaFileService.mergeChunks(companyId, fileMd5, chunkTotal, uploadFileParamsDto);

    }


}