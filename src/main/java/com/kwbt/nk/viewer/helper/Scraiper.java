package com.kwbt.nk.viewer.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;
import com.ui4j.api.event.DocumentLoadEvent;

public class Scraiper implements AutoCloseable {

    private final static Logger logger = LoggerFactory.getLogger(Scraiper.class);

    private final static PageConfiguration conf = new PageConfiguration().setUserAgent("Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:43.0) Gecko/20100101 Firefox/43.0");

    /**
     * WebKitは2重起動を防止するため、JVM起動時に生成しておく
     */
    static {

        // よくわからんけど、マニュアルからコピペ
        System.setProperty("ui4j", "true");
    }

    @Override
    public void close() throws Exception {
        BrowserFactory.getWebKit().shutdown();
    }

    public Page getPage() {

        Page page = BrowserFactory.getWebKit().navigate("http://www.jra.go.jp/", conf);
        page.hide();
        page.addDocumentListener((DocumentLoadEvent dle) -> {
            try {

                // システムが提供する一時フォルダに、スナップショットを保存
                String fileName = System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID();

                page.captureScreen(new FileOutputStream(new File(fileName + ".png")));

                logger.info("SI4J Snapshot file: {}", fileName);

            } catch (FileNotFoundException e) {
                logger.error("WebKit：Page生成エラー", e);
                e.printStackTrace();
            }
        });

        return page;
    }

}
