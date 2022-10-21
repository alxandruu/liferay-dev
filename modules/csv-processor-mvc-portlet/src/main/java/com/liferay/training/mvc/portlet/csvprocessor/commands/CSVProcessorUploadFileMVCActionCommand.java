package com.liferay.training.mvc.portlet.csvprocessor.commands;


import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.mvc.portlet.csvprocessor.constants.CSVProcessorMVCPortletKeys;
import com.liferay.training.people.service.LoansLocalServiceUtil;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.osgi.service.component.annotations.Component;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + CSVProcessorMVCPortletKeys.CSVPROCESSORMVC,
                "mvc.command.name=" + CSVProcessorMVCPortletKeys.COMMAND_ACTION_UPLOAD_CSV
        },
        service = MVCActionCommand.class
)
public class CSVProcessorUploadFileMVCActionCommand extends BaseMVCActionCommand {

    public static final long MAX_SIZE = 5242880;    // 5 Megabytes = 5242880 Bytes

    @Override
    protected void doProcessAction(ActionRequest actionRequest, ActionResponse actionResponse) {

        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);

        // Get Upload File
        UploadPortletRequest uploadPortletRequest = PortalUtil.getUploadPortletRequest(actionRequest);
        File file = uploadPortletRequest.getFile("uploadCSV");
        long fileSize = uploadPortletRequest.getSize("uploadCSV");

        if (Validator.isNotNull(file)) {
            _log.debug("upload file data => name = " + file.getName() + " , path = " + file.getAbsolutePath() + " , size = " + fileSize);
        }

        if (fileSize > MAX_SIZE) {
            SessionErrors.add(actionRequest, "file_max_size");
        } else if (!FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("csv")) {
            SessionErrors.add(actionRequest, "file_format");
        } else {
            // Logic (can be integrated with a service-builder)

            // Parse CSV
            CSVParser csvReader = null;
            try {
                csvReader = CSVParser.parse(
                        file,
                        Charset.forName(StringPool.UTF8),
                        CSVFormat.DEFAULT
                                .withDelimiter(StringPool.SEMICOLON.charAt(0))
                                .withFirstRecordAsHeader()
                );
            } catch (Exception e) {
                _log.error("cannot parse CSV", e);
            }

            // Reading CSV
            if (Validator.isNotNull(csvReader)) {
                _log.debug("csv file reading correctly");
                for (CSVRecord csvRecord : csvReader) {
                    //Logic for reading the CSV file (can be integrated with a service-builder)
                    // String example = csvRecord.get(0);
                    try {
                        String name = csvRecord.get(0);
                        String surname = csvRecord.get(1);
                        String birthDate = csvRecord.get(2);
                        String totalLoan = csvRecord.get(3);
                        if(Validator.isNotNull(name) && Validator.isNotNull(surname) && Validator.isNotNull(birthDate) && Validator.isNotNull(totalLoan)){
                            LoansLocalServiceUtil.insertLoan(name, surname, new SimpleDateFormat("yyyy-MM-dd").parse(birthDate), Float.parseFloat(totalLoan));
                        } else {
                            _log.debug("cannot insert Loan row" + csvRecord.getRecordNumber() + ", problem with required fields");
                        }
                    } catch (ParseException e) {
                        _log.error("cannot insert Loan row" + csvRecord.getRecordNumber(), e);
                    }
                }
            }
        }

        // Delete uploaded file
        if (Validator.isNotNull(file)) {
            file.delete();
        }

    }

    private static final Log _log = LogFactoryUtil.getLog(CSVProcessorUploadFileMVCActionCommand.class);

}