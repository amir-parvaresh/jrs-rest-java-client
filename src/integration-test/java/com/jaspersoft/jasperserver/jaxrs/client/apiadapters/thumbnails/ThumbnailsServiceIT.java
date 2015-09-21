package com.jaspersoft.jasperserver.jaxrs.client.apiadapters.thumbnails;

import com.jaspersoft.jasperserver.dto.thumbnails.ResourceThumbnail;
import com.jaspersoft.jasperserver.jaxrs.client.core.JasperserverRestClient;
import com.jaspersoft.jasperserver.jaxrs.client.core.RestClientConfiguration;
import com.jaspersoft.jasperserver.jaxrs.client.core.Session;
import com.jaspersoft.jasperserver.jaxrs.client.core.enums.JRSVersion;
import com.jaspersoft.jasperserver.jaxrs.client.core.enums.MimeType;
import com.jaspersoft.jasperserver.jaxrs.client.core.enums.RequestMethod;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static java.util.Arrays.asList;

/**
* Integration tests for {@link ThumbnailsService}
*/
public class ThumbnailsServiceIT {

    private RestClientConfiguration config;
    private JasperserverRestClient client;
    private Session session;

    @BeforeMethod
    public void before() {
        config = new RestClientConfiguration("http://localhost:4444/jasperserver-pro");
        config.setAcceptMimeType(MimeType.JSON);
        config.setContentMimeType(MimeType.JSON);
        config.setJrsVersion(JRSVersion.v6_0_0);
        config.setLogHttp(true);

        client = new JasperserverRestClient(config);
        session = client.authenticate("superuser", "superuser");
    }

    @Test
    /**
     * Batch thumbnails operation
     */
    public void should_return_list_of_thumbnails_with_default_request_method() {
        List<ResourceThumbnail> entity = session.thumbnailsService()
                .thumbnails()
                .reports("/public/Samples/Reports/08g.UnitSalesDetailReport",
                        "/public/Samples/Reports/11g.SalesByMonthReport")
                .defaultAllowed(true)
                .get()
                .getEntity()
                .getThumbnails();
        Assert.assertNotNull(entity);
        Assert.assertTrue(entity.size() == 2);
    }

    @Test
    /**
     * Batch thumbnails operation
     */
    public void should_return_list_of_thumbnails_with_get_request_method() {
        List<ResourceThumbnail> entity = session.thumbnailsService()
                .thumbnails()
                .reports(asList("/public/Samples/Reports/08g.UnitSalesDetailReport",
                        "/public/Samples/Reports/11g.SalesByMonthReport"))
                .defaultAllowed(true).requestMethod(RequestMethod.GET)
                .get()
                .getEntity()
                .getThumbnails();
        Assert.assertNotNull(entity);
        Assert.assertTrue(entity.size() == 2);
    }

    @Test
    /**
     * Single thumbnail operation
     */
    public void should_return_single_thumbnail_as_stream() throws IOException {
        InputStream entity = session.thumbnailsService()
                .thumbnail()
                .report("/public/Samples/Reports/08g.UnitSalesDetailReport")
                .defaultAllowed(true)
                .get()
                .getEntity();
        Assert.assertNotNull(entity);
    }

    @AfterMethod
    public void after() {
        session.logout();
        client = null;
        config = null;
        session = null;
    }
}