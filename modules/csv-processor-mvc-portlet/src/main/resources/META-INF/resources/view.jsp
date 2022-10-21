<%@ include file="/init.jsp" %>

<portlet:actionURL name="<%= CSVProcessorMVCPortletKeys.COMMAND_ACTION_UPLOAD_CSV %>" var="uploadCSVURL" />
<portlet:resourceURL id="<%= CSVProcessorMVCPortletKeys.COMMAND_RESOURCE_DOWNLOAD_CSV %>" var="downloadCSVURL" />

<liferay-ui:error key="file_max_size" message="csvprocessormvc.error.file.maxSize"/>
<liferay-ui:error key="file_format" message="csvprocessormvc.error.file.fileFormat"/>

<div class="m-1">
    <form action="<%= uploadCSVURL %>" method="post" enctype="multipart/form-data">
        <h4 ><liferay-ui:message key="csvprocessormvc.view.uploadCSV"/></h4>
        <input class="form-control" id="upload" name="<portlet:namespace/>uploadCSV" type="file"  accept=".csv" required />
        <button class="btn btn-primary"  type="submit" >
            <liferay-ui:message key="csvprocessormvc.view.button.upload"/>
        </button>
    </form>

    <button class="btn btn-secondary mt-2" id="downloadCSV" type="button">
        <liferay-ui:message key="csvprocessormvc.view.button.download"/>
    </button>
</div>


<script type="text/javascript">
     document.querySelector('#downloadCSV').addEventListener("click", function (){
         window.open('<%= downloadCSVURL %>', '_blank');
     });
</script>