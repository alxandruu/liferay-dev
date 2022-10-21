package com.liferay.training.mvc.portlet.inside.panel.application.list;

import com.liferay.application.list.BasePanelApp;

import com.liferay.application.list.PanelApp;
import com.liferay.application.list.constants.PanelCategoryKeys;

import com.liferay.portal.kernel.model.Portlet;

import com.liferay.training.mvc.portlet.inside.panel.constants.InsidePanelMVCPortletKeys;

import org.osgi.service.component.annotations.Component;

import org.osgi.service.component.annotations.Reference;


@Component(
        immediate = true,

        property = {
                "panel.category.key=" + PanelCategoryKeys.SITE_ADMINISTRATION_CONTENT,
                "panel.app.order:Integer=10009"
        },
        service = PanelApp.class
)
public class InsidePanelModulePanelApp extends BasePanelApp {
    @Override
    public String getPortletId() {
        return InsidePanelMVCPortletKeys.INSIDEPANELMVC;
    }




    @Override
    @Reference(
            target = "(javax.portlet.name=" + InsidePanelMVCPortletKeys.INSIDEPANELMVC + ")",
            unbind = "-"
    )
    public void setPortlet(Portlet portlet) {
        super.setPortlet(portlet);
    }


}
