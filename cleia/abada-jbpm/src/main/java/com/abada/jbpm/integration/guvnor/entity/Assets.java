/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.abada.jbpm.integration.guvnor.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author katsu
 */
@XStreamAlias(value = "assets")
public class Assets {
    private String title;
    private String description;
    private String author;
    private String published;
    private String binaryLink;
    private String sourceLink;
    private String refLink;
    private AssetMetada metadata;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getBinaryLink() {
        return binaryLink;
    }

    public void setBinaryLink(String binaryLink) {
        this.binaryLink = binaryLink;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getRefLink() {
        return refLink;
    }

    public void setRefLink(String refLink) {
        this.refLink = refLink;
    }

    public AssetMetada getMetadata() {
        return metadata;
    }

    public void setMetadata(AssetMetada metadata) {
        this.metadata = metadata;
    }        
}
