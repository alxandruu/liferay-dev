package com.liferay.training.mvc.portlet.inside.panel.portlet;


import com.liferay.training.mvc.portlet.inside.panel.constants.InsidePanelMVCPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import javax.portlet.*;

import org.osgi.service.component.annotations.Component;

import java.io.IOException;

/**
 * @author Alex
 */
@Component(
        immediate = true,
        property = {
                "com.liferay.portlet.display-category=category.hidden",
                "com.liferay.portlet.header-portlet-css=/css/main.css",
                "javax.portlet.display-name=InsidePanelMVC",
                "javax.portlet.init-param.template-path=/",
                "javax.portlet.init-param.view-template=/view.jsp",
                "javax.portlet.name=" + InsidePanelMVCPortletKeys.INSIDEPANELMVC,
                "javax.portlet.resource-bundle=content.Language",
                "javax.porXtlet.security-role-ref=power-user,user"
        },
        service = Portlet.class
)
public class InsidePanelMVCPortlet extends MVCPortlet {
    @Override
    public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
            throws IOException, PortletException {
        include("/view.jsp", renderRequest, renderResponse);

    }
}