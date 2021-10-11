package org.jitsi.xmpp.extensions.colibri2;

import org.jitsi.utils.*;
import org.jitsi.xmpp.extensions.*;
import org.jitsi.xmpp.extensions.jingle.*;

import javax.xml.namespace.*;
import java.util.*;

public class Media
    extends AbstractPacketExtension
{
    /**
     * The XML element name of the Colibri2 Media element.
     */
    public static final String ELEMENT = "media";

    /**
     * The XML colibri2 namespace of the Jitsi
     * Videobridge <tt>conference-modify</tt> IQ.
     */
    public static final String NAMESPACE = ConferenceModifyIQ.NAMESPACE;

    /**
     * The qualified name of the element.
     */
    public static final QName QNAME = new QName(NAMESPACE, ELEMENT);

    public static final String TYPE_ATTR_NAME = "type";

    /**
     * Construct a Media.  Needs to be public for DefaultPacketExtensionProvider to work.
     */
    public Media()
    {
        super(NAMESPACE, ELEMENT);
    }

    /**
     * Construct a media from a builder - used by Builder#build().
     */
    private Media(Builder b)
    {
        super(NAMESPACE, ELEMENT);

        if (b.type == null)
        {
            throw new IllegalArgumentException("Media type must be set");
        }
        super.setAttribute(TYPE_ATTR_NAME, b.type.toString());

        for (PayloadTypePacketExtension pt: b.payloadTypes) {
            super.addChildExtension(pt);
        }

        for (RTPHdrExtPacketExtension ext: b.rtpHeaderExtensions) {
            super.addChildExtension(ext);
        }
    }

    /**
     * Get the media type of this media.
     */
    public MediaType getType()
    {
        /* TODO: handle invalid media types at XML parse time?  This will throw at get-time. */
        return MediaType.parseString(super.getAttributeAsString(TYPE_ATTR_NAME));
    }

    /**
     * Get the payload types of this media.
     */
    public List<PayloadTypePacketExtension> getPayloadTypes()
    {
        return super.getChildExtensionsOfType(PayloadTypePacketExtension.class);
    }

    /**
     * Get the RTP header extensions of this media.
     */
    public List<RTPHdrExtPacketExtension> getRtpHdrExts()
    {
        return super.getChildExtensionsOfType(RTPHdrExtPacketExtension.class);
    }

   /**
     * Get a builder for Media objects.
     */
    public static Builder getBuilder()
    {
        return new Builder();
    }

    /**
     * Builder for Transport objects.
     */
    public static final class Builder
    {
        /**
         * The media type of the media object being built.
         */
        MediaType type = null;

        /**
         * The <tt>payload-type</tt> elements defined by XEP-0167: Jingle RTP
         * Sessions associated with this <tt>media</tt>.
         */
        private final List<PayloadTypePacketExtension> payloadTypes
            = new ArrayList<>();

        /**
         * The <tt>rtp-hdrext</tt> elements defined by XEP-0294: Jingle RTP
         * Header Extensions Negotiation associated with this media.
         */
        private final List<RTPHdrExtPacketExtension> rtpHeaderExtensions
            = new ArrayList<>();

        /**
         * Sets the media type for the media being built.
         */
        public void setType(MediaType t)
        {
            type = t;
        }

        /**
         * Adds a payload type to the media being built.
         */
        public void addPayloadType(PayloadTypePacketExtension pt)
        {
            payloadTypes.add(pt);
        }

        /**
         * Adds an RTP header extension to the media being built.
         */
        public void addRtpHdrExt(RTPHdrExtPacketExtension ext)
        {
            rtpHeaderExtensions.add(ext);
        }

        /* TODO: add something to set values from higher-level Jingle structures. */

        private Builder()
        {
        }
        
        public Media build()
        {
            return new Media(this);
        }
    }
}