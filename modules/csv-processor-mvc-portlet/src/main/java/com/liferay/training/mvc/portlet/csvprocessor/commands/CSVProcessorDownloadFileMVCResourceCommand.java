package com.liferay.training.mvc.portlet.csvprocessor.commands;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.training.mvc.portlet.csvprocessor.constants.CSVProcessorMVCPortletKeys;
import com.liferay.training.people.model.Loans;
import com.liferay.training.people.service.LoansLocalServiceUtil;
import org.osgi.service.component.annotations.Component;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


@Component(
        immediate = true,
        property = {
                "javax.portlet.name=" + CSVProcessorMVCPortletKeys.CSVPROCESSORMVC,
                "mvc.command.name=" + CSVProcessorMVCPortletKeys.COMMAND_RESOURCE_DOWNLOAD_CSV
        },
        service = MVCResourceCommand.class
)
public class CSVProcessorDownloadFileMVCResourceCommand extends BaseMVCResourceCommand {


    @Override
    protected void doServeResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse) throws Exception {
        ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        Calendar cNow = Calendar.getInstance();
        String outputName = "example_" + sdf.format(cNow.getTime()) + ".csv";

        File output = null;
        BufferedWriter bw = null;

        try {
            String split = StringPool.SEMICOLON;

            output = new File(System.getProperty("java.io.tmpdir") + File.separator + outputName);
            _log.debug("file download: " + output.getAbsolutePath());

            //Logic (can be integrated with a service-builder)

            // csv tmp file - write header

            bw = Files.newBufferedWriter(output.toPath(), Charset.defaultCharset(), StandardOpenOption.CREATE);
            bw.write("name" + split + "surname" + split + "birth_date" + split + "total_loan");
            bw.newLine();
            List<Loans> loans = LoansLocalServiceUtil.getLoanses(-1 , -1);
            //Logic at adding lines in the CSV file (can be integrated with a service-builder)
            // csv tmp file - write body
            for(Loans l: loans){
                StringBuilder sb = new StringBuilder();
                sb.append(l.getName());
                sb.append(split);
                sb.append(l.getSurname());
                sb.append(split);
                sb.append(l.getBirth());
                sb.append(split);
                sb.append(l.getTotal_loan());
                sb.append(split);
                bw.write(sb.toString());
                bw.newLine();
            }

        } catch (Exception e) {
            _log.error("cannot download file", e);
        } finally {
            if (bw != null) {
                bw.close();
            }
            if (output != null) {
                //Downloads file
                byte[] bytes = Files.readAllBytes(Paths.get(output.getAbsolutePath()));
                PortletResponseUtil.sendFile(resourceRequest, resourceResponse, outputName, bytes, ContentTypes.TEXT_CSV);
                //Delete temporary file
                output.delete();
            }

        }
    }

    private static final Log _log = LogFactoryUtil.getLog(CSVProcessorDownloadFileMVCResourceCommand.class);
}
