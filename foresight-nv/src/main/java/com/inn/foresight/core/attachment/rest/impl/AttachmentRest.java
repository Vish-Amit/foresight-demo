package com.inn.foresight.core.attachment.rest.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.inn.commons.io.IOUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.attachment.service.IAttachmentService;
import com.inn.foresight.core.attachment.utils.UnzipUtility;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.Constants;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.InfraConstants;

@Path("AttachmentData")
public class AttachmentRest {

    private static Logger logger = LogManager.getLogger(AttachmentRest.class);

    public AttachmentRest() {
    	super();
    }

    /** The i attachment service. */
    @Autowired
    private IAttachmentService iAttachmentService;

    /**
     * @param inputFile
     * @param fileName
     * @return Response
     * @throws RestException
     * @throws Exception
     */
    @POST
    @Path("/saveAttachment")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/html")
    public Response saveAttachment(@Multipart("file") InputStream inputStream, @QueryParam("path") String path,
            @QueryParam("isHDFS") boolean isHDFS) {

        try {
            if (inputStream != null) {
                if (path != null) {
                    if (isHDFS) {
                        iAttachmentService.saveAttachmentInHDFS(inputStream, path);
                        logger.info("File saved in hdfs");
                        return Response.ok(Constants.ATTACHMENT_SUCCESS_RESPONSE).build();
                    } else {
                        iAttachmentService.saveAttachment(inputStream, path);
                        logger.info(Constants.SAVED_MESSAGE);
                        return Response.ok(Constants.ATTACHMENT_SUCCESS_RESPONSE).build();
                    }
                } else {
                    logger.error(Constants.MISSING_FILE_PATH_MESSAGE);
                    return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
                }
            } else {
                logger.error(Constants.MISSING_FILE);
                return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
            }
        } catch (IOException e) {
            logger.error(Constants.ERROR_LOG + e.getMessage());
            return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Failed to close stream : " + e.getMessage());
                }
            }
        }
    }

    /**
     * 
     * getAttachment method is recieve path and return file is given path
     * 
     * @param path
     * @return
     * @throws RestException
     */
    @GET
    @Path("/getAttachment")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response getAttachment(@QueryParam("path") String path) {
        try {
            if (path != null) {
                File targetFile = new File(ConfigUtil.getConfigProp(ConfigUtil.ATTACHMENT_FILE_PATH) + path);

                ResponseBuilder response = Response.ok(IOUtils.toByteArray(new FileInputStream(targetFile)));
                response.header("Content-Disposition", InfraConstants.ATTACHMENT_FILENAME + targetFile.getName() + "\"");
                return response.build();

            } else {
            	  logger.error(Constants.MISSING_FILE_PATH_MESSAGE);
                return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
            }
        } catch (FileNotFoundException e) {
            logger.error(Constants.MISSING_FILE);
            return Response.ok(Constants.GET_ATTACHMENT_FILE_NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error while getting attachment FILE, err :  " + e.getMessage());
            return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
        }
    }

    @GET
    @Path("/getZipAttachment")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response getZipAttachment(@QueryParam("zipFilePath") String zipFilePath,
            @QueryParam("destDirectory") String destDirectory) {
        try {
            if (zipFilePath != null) {
                String finalPath = ConfigUtil.getConfigProp(ConfigUtil.ATTACHMENT_FILE_PATH) + zipFilePath + ForesightConstants.FORWARD_SLASH
                        + destDirectory;
                File targetFile = new File(finalPath + InfraConstants.ZIP_EXTENSION);
                if (targetFile.exists()) {

                    logger.info("deleteing Old zip File and creating new zip");
                    if(targetFile.delete())
                    	logger.info("File Deleted Successfully.");

                    List<String> files = new ArrayList<>();
                    for (String fileName : new File(finalPath).list()) {
                        files.add(finalPath + ForesightConstants.FORWARD_SLASH + fileName);
                    }
                    UnzipUtility.createZipFileFromFileList(files, finalPath + InfraConstants.ZIP_EXTENSION);
                    ResponseBuilder response = Response.ok(IOUtils.toByteArray(new FileInputStream(targetFile)));
                    response.header(Constants.CONTENT_DISPOSITION, InfraConstants.ATTACHMENT_FILENAME + targetFile.getName() + "\"");
                    return response.build();
                } else {

                    List<String> files = new ArrayList<>();
                    for (String fileName : new File(finalPath).list()) {
                        files.add(finalPath + ForesightConstants.FORWARD_SLASH  + fileName);
                    }
                    UnzipUtility.createZipFileFromFileList(files, finalPath + InfraConstants.ZIP_EXTENSION);
                    ResponseBuilder response = Response.ok(IOUtils.toByteArray(new FileInputStream(targetFile)));
                    response.header(Constants.CONTENT_DISPOSITION, InfraConstants.ATTACHMENT_FILENAME + targetFile.getName() + "\"");
                    return response.build();

                }

            } else {
                return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
            }
        } catch (FileNotFoundException e) {
        	logger.error(Constants.MISSING_FILE);
            return Response.ok(Constants.GET_ATTACHMENT_FILE_NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error while getting attachment, err :  " + e.getMessage());
            return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
        }
    }

    /**
     * deleteAttachment method is take file name with path and delete given file
     * 
     * @param path
     * @return
     * @throws RestException
     */

    @POST
    @Path("/deleteAttachment")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response deleteAttachment(@QueryParam("path") String path) {

        File targetFile = null;

        try {
            if (path != null) {
                String filePath = ConfigUtil.getConfigProp(ConfigUtil.ATTACHMENT_FILE_PATH) + path;

                targetFile = new File(filePath);

                logger.info("deleting file : " + targetFile.getAbsolutePath());

                if (targetFile.exists()) {
                    if (targetFile.delete()) {
                        logger.info("Deleted file from : " + targetFile.getAbsolutePath());
                        ResponseBuilder response = Response.ok(Constants.ATTACHMENT_SUCCESS_JSON);
                        return response.build();
                    } else {
                        ResponseBuilder response = Response.ok(Constants.ATTACHMENT_FAILURE_JSON);
                        return response.build();
                    }
                } else {
                    return Response.ok(Constants.GET_ATTACHMENT_FILE_NOT_FOUND).build();
                }
            } else {
                return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
            }
        } catch (Exception e) {
            logger.error("Error while deleting attachment, err :  " + e.getMessage());
            return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
        }
    }

    /**
     * @param inputFile
     * @param fileName
     * @return Response
     * @throws RestException
     * @throws Exception
     */
    @POST
    @Path("/saveAttachmentWithAbsolutePath")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/html")
    public Response saveAttachmentWithAbsolutePath(@Multipart("file") InputStream inputStream,
            @QueryParam("path") String path) {
        try {
            if (inputStream != null) {
                if (path != null) {

                    logger.info("Saving file as : " + path);
                    File targetFile = new File(path);

                    FileUtils.copyInputStreamToFile(inputStream, targetFile);

                    logger.info("File saved");
                    return Response.ok(Constants.ATTACHMENT_SUCCESS_RESPONSE).build();
                } else {
                    return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
                }
            } else {
                logger.error("File not found...");
                return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
            }
        } catch (IOException e) {
            return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
        } finally {
            try {
            	if(inputStream != null)
                inputStream.close();
            } catch (IOException e) {
                logger.error("Error while saving attachment in path , err : " + e.getMessage());
            }
        }
    }

    /**
     * 
     * getAttachment method is recieve path and return file is given path
     * 
     * @param path
     * @return
     * @throws RestException
     */
    @SuppressWarnings("resource")
    @GET
    @Path("/getAttachmentWithAbsolutePath")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response getAttachmentWithAbsolutePath(@QueryParam("path") String path) {
        try {
            if (path != null) {
                File targetFile = new File(path);

                logger.info("getting data from file : " + targetFile.getAbsolutePath());
                logger.info("File Size : " + IOUtils.toByteArray(new FileInputStream(targetFile)).length);
                ResponseBuilder response = Response.ok(IOUtils.toByteArray(new FileInputStream(targetFile)));
                response.header(Constants.CONTENT_DISPOSITION, "attachment; filename=\"" + targetFile.getName() + "\"");
                return response.build();

            } else {
                logger.error("File path not mentioned...");
                return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found");
            return Response.ok(Constants.GET_ATTACHMENT_FILE_NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Error while getting attachment, err :  " + e.getMessage());
            return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
        }
    }

    @GET
    @Path("/moveSMQueueFiles")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public String moveSMQueueFiles(@QueryParam("path") String path) {
        try {
            logger.info("Going to move smqueue files {}", path);
            if (!Strings.isNullOrEmpty(path)) {
                File tempFilePath = new File(ConfigUtil.getConfigProp(ConfigUtil.ATTACHMENT_SHARED_DRIVE_PATH) + path);
                File actualfile = getFilePathFromTempPath(tempFilePath.getAbsolutePath());
                if (!actualfile.exists() || !actualfile.isDirectory()) {
                    actualfile.mkdirs();
                } else {
                    FileUtils.cleanDirectory(actualfile);
                }
                boolean status = tempFilePath.renameTo(actualfile);
                if (status) {
                    if(tempFilePath.delete())
                    	logger.info("file deleted successfully.");
                    return Constants.ATTACHMENT_SUCCESS_JSON;
                } else {
                    throw new RestException("Unable to move files.");
                }
            } else {
                throw new RestException("Invalid filepath");
            }
        } catch (RestException e) {
            throw new RestException(e.getMessage());
        } catch (Exception e) {
            logger.error("Error moving file : " + Utils.getStackTrace(e));
            throw new RestException("Unable for move files");
        }
    }

    private File getFilePathFromTempPath(String fileName) {
        return new File(StringUtils.removeEnd(fileName, Constants.TEMP));
    }

    /**
     * @param inputFile
     * @param fileName
     * @return Response
     * @throws RestException
     * @throws Exception
     */
    @POST
    @Path("/saveSMQueueAttachment")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("text/html")
    public Response saveSmqueueAttachment(@Multipart("file") InputStream inputStream, @QueryParam("path") String path,
            @QueryParam("targetDir") String targetDirPath,
            @QueryParam("fileWithoutExtension") String fileWithoutExtension) {
        try {
            if (inputStream != null) {
                if (path != null) {

                    logger.info("Saving smqueue file as : " + path);
                    File targetFile = new File(
                            ConfigUtil.getConfigProp(ConfigUtil.ATTACHMENT_SHARED_DRIVE_PATH) + path);

                    File targetDir = new File(
                            ConfigUtil.getConfigProp(ConfigUtil.ATTACHMENT_SHARED_DRIVE_PATH) + targetDirPath);

                    deleteExistingFiles(targetDir, fileWithoutExtension);

                    FileUtils.copyInputStreamToFile(inputStream, targetFile);

                    logger.info("File saved");
                    return Response.ok(Constants.ATTACHMENT_SUCCESS_RESPONSE).build();
                } else {
                    logger.error("File path not mentioned...");
                    return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
                }
            } else {
                logger.error("File not found...");
                return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
            }
        } catch (IOException e) {
            logger.error("Error while saving SM queue attachment, err : " + e.getMessage());
            return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
        } finally {
            try {
            	if(inputStream != null)
                inputStream.close();
            } catch (IOException e) {
                logger.error("Error while saving attachment, err : " + e.getMessage());
            }
        }
    }

    @GET
    @Path("/downloadLogs")
    @Produces("application/zip")
    public Response downloadLogs(@QueryParam("path") String path) {
        try {
            ResponseBuilder response = Response.ok(getAllFile(path));
            response.header(Constants.CONTENT_DISPOSITION, "attachment; filename=\"" + "LOGS.zip\"");
            return response.build();
        } catch (Exception e) {
            logger.error("Error while saving attachment, err : " + e.getMessage());
            return Response.ok(Constants.ATTACHMENT_FAILURE_RESPONSE).build();
        }
    }

    private void removezip(String zipFileSourceDir) {
        try {
            if(new File(zipFileSourceDir).delete())
            	logger.info("zip file deleted successfully.");
        } catch (Exception e) {
        	logger.error("Error while removing zip, err : " + e.getMessage());
        }
    }

    private InputStream getAllFile(String path) throws IOException {
        String creatZipFile = creatZipFile(path);
        try(InputStream inputStream =new FileInputStream(new File(creatZipFile))) {
            removezip(creatZipFile);
            return inputStream;
        } 
       
    }

    public static String creatZipFile(String zipFileSourceDir) throws IOException {
        File file = new File(zipFileSourceDir);
        File[] files = file.listFiles();
        String zipFileDestPath = zipFileSourceDir.substring(0, zipFileSourceDir.lastIndexOf(ForesightConstants.FORWARD_SLASH)) + File.separator
                + "LOGS.zip";
        if (files != null && files.length > 0) {
            FileOutputStream fos = new FileOutputStream(new File(zipFileDestPath));
            ZipOutputStream zos = new ZipOutputStream(fos);
            if (file.exists()) {
                for (File subFile : file.listFiles()) {
                    if (StringUtils.endsWithIgnoreCase(subFile.getName(), ".log"))
                        addToZipFile(subFile, zos);
                }
            }
            zos.close();
            fos.close();
            return zipFileDestPath;
        } else {
            throw new RestException("Files doesn't exist");
        }

    }

    public static void addToZipFile(File file, ZipOutputStream zos) throws IOException {
    	try(FileInputStream fis = new FileInputStream(file)) {
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }
    }
    }

    private void deleteExistingFiles(File targetDir, final String fileWithoutExtension) {
        if (targetDir != null && targetDir.isDirectory()) {
            File[] files = null;
            files = targetDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File directory, String name) {
                    return name.startsWith(fileWithoutExtension);
                }
            });

            if (files.length != 0) {
                logger.info("{} SMQueue files found to delete in {}", files.length, targetDir);
                for (File file : files) {
                    if(file.delete())
                    	logger.info("file deleted successfully.");
                }
            }
        }
    }
    
	@GET
	@Path("/downloadFromHDFS")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response downloadFromHDFS(@QueryParam("filePath") String filePath)
			throws IOException {
		return Response.ok(iAttachmentService.downloadFromHDFS(filePath)).build();
	}
	
	
	@GET
	@Path("/isFileExistInHDFS")
	@Produces(MediaType.MULTIPART_FORM_DATA)
	public Response isFileExistInHDFS(@QueryParam("filePath") String filePath)
			throws IOException {
		return Response.ok(iAttachmentService.isFileExistInHDFS(filePath)).build();
	}
}
