package org.md2k.phonesensor.phone;

import android.content.Context;
import android.os.Build;
import org.md2k.datakitapi.source.METADATA;
import org.md2k.datakitapi.source.platform.Platform;
import org.md2k.datakitapi.source.platform.PlatformBuilder;
import org.md2k.datakitapi.source.platform.PlatformType;

/**
 * Copyright (c) 2015, The University of Memphis, MD2K Center
 * - Syed Monowar Hossain <monowar.hossain@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

public class PhoneSensorPlatform {
    Context context;
    private static PhoneSensorPlatform instance=null;
    public static PhoneSensorPlatform getInstance(Context context){
        if(instance==null)
            instance=new PhoneSensorPlatform(context);
        return instance;
    }
    public String getId() {
        return null;
    }
    public String getOS(){
        return Build.VERSION.RELEASE;
    }
    public String getType(){
        return PlatformType.PHONE;
    }
    public String getManufacturer(){
        return Build.MANUFACTURER;
    }
    public String getName(){
        return android.os.Build.MODEL;
    }

    private PhoneSensorPlatform(Context context) {
        this.context = context;
    }
    public Platform getPlatform() {
        return new PlatformBuilder().
                setId(getId()).setType(getType()).
                setMetadata(METADATA.OPERATING_SYSTEM, getOS()).setMetadata(METADATA.MANUFACTURER, getManufacturer()).setMetadata(METADATA.MODEL, getName()).build();
    }
}
