package com.googlecode.easyec.spirit.web.webservice.factory.impl;

import com.googlecode.easyec.spirit.web.webservice.Envelope;
import com.googlecode.easyec.spirit.web.webservice.factory.EnvelopeFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.Assert;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import static java.nio.charset.Charset.forName;

/**
 * Spring OXM支持的信封数据转换工厂类
 *
 * @author JunJie
 */
public class Jaxb2MarshallerEnvelopeFactory implements EnvelopeFactory {

    private static final Logger logger = LoggerFactory.getLogger(JaxbContextEnvelopeFactory.class);

    private Charset charset = forName("UTF-8");
    private Jaxb2Marshaller jaxb2Marshaller;

    public Jaxb2MarshallerEnvelopeFactory(Jaxb2Marshaller jaxb2Marshaller) {
        Assert.notNull(jaxb2Marshaller, "Jaxb2Marshaller object is null.");

        this.jaxb2Marshaller = jaxb2Marshaller;
    }

    /**
     * 设置字符集
     *
     * @param charset 字符集
     */
    public void setCharset(String charset) {
        this.charset = forName(charset);
    }

    public String asXml(Envelope envelope) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            jaxb2Marshaller.marshal(envelope, new StreamResult(bos));
            return new String(bos.toByteArray(), charset);
        } catch (XmlMappingException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bos);
        }

        return null;
    }

    public Envelope asEnvelope(String xml) {
        InputStream bis = IOUtils.toInputStream(xml, charset);

        try {
            Object o = jaxb2Marshaller.unmarshal(new StreamSource(bis));
            if (null != o) {
                Assert.isInstanceOf(Envelope.class, o);

                return (Envelope) o;
            }

            logger.trace("Unmarshal object is null.");
        } catch (XmlMappingException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bis);
        }

        return null;
    }
}
