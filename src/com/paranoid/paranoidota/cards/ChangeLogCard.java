/*
 * Copyright 2014 ParanoidAndroid Project
 *
 * This file is part of Paranoid OTA.
 *
 * Paranoid OTA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Paranoid OTA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Paranoid OTA.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.paranoid.paranoidota.cards;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;


import com.paranoid.paranoidota.R;
import com.paranoid.paranoidota.widget.Card;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChangeLogCard extends Card {

    private TextView outtext;
    private String HTML;

    public ChangeLogCard(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super(context, attrs, savedInstanceState);

        setTitle(R.string.changelog);
        setLayoutId(R.layout.card_changelog);

        outtext= (TextView) findViewById(R.id.changelog); //change id if needed!!!

        new RetrieveChangeLogTask().execute("");

    }

    @Override
    public boolean canExpand() {
        return false;
    }

    private class RetrieveChangeLogTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("https://gist.github.com/BryanSmithDev/fc4eb534ab5a0af67a0a/raw/"); //URL!
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpGet, localContext);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String result = "";

            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    result += line + "\n";
                    HTML = result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return HTML;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null)
                outtext.setText(""+result);
            else
                outtext.setText(R.string.error_http_data_error);
        }
    }


}
