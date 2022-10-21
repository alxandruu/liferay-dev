package com.liferay.training.mvc.portlet.csvprocessor.application.list;

import com.liferay.application.list.BasePanelApp;
import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.training.mvc.portlet.csvprocessor.constants.CSVProcessorMVCPortletKeys;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


@Component(
        immediate = true,

        property = {
                "panel.category.key=" + PanelCategoryKeys.SITE_ADMINISTRATION_CONTENT,
                "panel.app.order:Integer=1"
        },
        service = PanelApp.class
)
public class CSVProcessorPanelApp extends BasePanelApp {
    @Override
    public String getPortletId() {
        return CSVProcessorMVCPortletKeys.CSVPROCESSORMVC;
    }




    @Override
    @Reference(
            target = "(javax.portlet.name=" + CSVProcessorMVCPortletKeys.CSVPROCESSORMVC + ")",
            unbind = "-"
    )
    public void setPortlet(Portlet portlet) {
        super.setPortlet(portlet);
    }


}
