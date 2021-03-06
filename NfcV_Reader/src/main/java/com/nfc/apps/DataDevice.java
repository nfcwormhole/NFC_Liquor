// THE PRESENT FIRMWARE WHICH IS FOR GUIDANCE ONLY AIMS AT PROVIDING CUSTOMERS 
// WITH CODING INFORMATION REGARDING THEIR PRODUCTS IN ORDER FOR THEM TO SAVE 
// TIME. AS A RESULT, STMICROELECTRONICS SHALL NOT BE HELD LIABLE FOR ANY 
// DIRECT, INDIRECT OR CONSEQUENTIAL DAMAGES WITH RESPECT TO ANY CLAIMS 
// ARISING FROM THE CONTENT OF SUCH FIRMWARE AND/OR THE USE MADE BY CUSTOMERS 
// OF THE CODING INFORMATION CONTAINED HEREIN IN CONNECTION WITH THEIR PRODUCTS.

package com.nfc.apps;

import android.app.Application;
import android.nfc.Tag;

public class DataDevice extends Application {
    private Tag currentTag;
    private String uid;
    private String techno;
    private String manufacturer;
    private String productName;
    private String dsfid;
    private String afi;
    private String memorySize;
    private String blockSize;
    private String icReference;
    private boolean basedOnTwoBytesAddress;
    private boolean MultipleReadSupported;
    private boolean MemoryExceed2048bytesSize;

    public Tag getCurrentTag() {
        return currentTag;
    }

    public void setCurrentTag(Tag currentTag) {
        this.currentTag = currentTag;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTechno() {
        return techno;
    }

    public void setTechno(String techno) {
        this.techno = techno;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDsfid() {
        return dsfid;
    }

    public void setDsfid(String dsfid) {
        this.dsfid = dsfid;
    }

    public String getAfi() {
        return afi;
    }

    public void setAfi(String afi) {
        this.afi = afi;
    }

    public String getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(String memorySize) {
        this.memorySize = memorySize;
    }

    public String getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(String blockSize) {
        this.blockSize = blockSize;
    }

    public String getIcReference() {
        return icReference;
    }

    public void setIcReference(String icReference) {
        this.icReference = icReference;
    }

    public boolean isBasedOnTwoBytesAddress() {
        return basedOnTwoBytesAddress;
    }

    public void setBasedOnTwoBytesAddress(boolean basedOnTwoBytesAddress) {
        this.basedOnTwoBytesAddress = basedOnTwoBytesAddress;
    }

    public boolean isMultipleReadSupported() {
        return MultipleReadSupported;
    }

    public void setMultipleReadSupported(boolean MultipleReadSupported) {
        this.MultipleReadSupported = MultipleReadSupported;
    }

    public boolean isMemoryExceed2048bytesSize() {
        return MemoryExceed2048bytesSize;
    }

    public void setMemoryExceed2048bytesSize(boolean MemoryExceed2048bytesSize) {
        this.MemoryExceed2048bytesSize = MemoryExceed2048bytesSize;
    }

}
