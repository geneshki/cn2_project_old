/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.tu.darmstadt.cn2.idb34;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import de.tu.darmstadt.cn2.idb34.util.ScalingUtilities;
import de.tu.darmstadt.cn2.idb34.util.ScalingUtilities.ScalingLogic;

public class ScaleActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Grab resources and layouts
        final int mDstWidth = getResources().getDimensionPixelSize(
                R.dimen.destination_width);
        final int mDstHeight = getResources().getDimensionPixelSize(
                R.dimen.destination_height);

        final Intent returnIntent = new Intent();
        if (getIntent().hasExtra("de.tu.darmstadt.cn2.idb34.ImageNames")) {
            final ArrayList<String> imageNames = getIntent()
                    .getStringArrayListExtra(
                            "de.tu.darmstadt.cn2.idb34.ImageNames");
            for (String imageName : imageNames) {
                final int mSourceId = getIntent().getIntExtra(imageName, 0);
                if (mSourceId != 0) {
                    final Bitmap scaledBitmap = fitButtonPressed(mDstWidth,
                            mDstHeight, mSourceId);
                    returnIntent.putExtra(imageName, scaledBitmap);
                } else {
                    setResult(RESULT_CANCELED);
                    finish();
                    return;
                }
            }
            setResult(Activity.RESULT_OK, returnIntent);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    /**
     * Invoked when pressing button for showing result of the "Fit" decoding
     * method
     * 
     * @return
     */
    protected Bitmap fitButtonPressed(int mDstWidth, int mDstHeight,
            int sourceId) {

        // Part 1: Decode image
        final Bitmap unscaledBitmap = ScalingUtilities.decodeResource(
                getResources(), sourceId, mDstWidth, mDstHeight,
                ScalingLogic.FIT);

        // Part 2: Scale image
        final Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(
                unscaledBitmap, mDstWidth, mDstHeight, ScalingLogic.FIT);
        unscaledBitmap.recycle();

        return scaledBitmap;
    }
}