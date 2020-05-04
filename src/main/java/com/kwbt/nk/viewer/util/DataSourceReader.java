package com.kwbt.nk.viewer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kwbt.nk.common.JsonData;

/**
 * データファイルをJSONで読み込むクラス
 *
 * @author baya
 */
public class DataSourceReader {

    private final static Logger logger = LoggerFactory.getLogger(DataSourceReader.class);

    public static Map<Integer, List<JsonData>> readJsonFile(File f) {

        try (FileReader fs = new FileReader(f);
                BufferedReader br = new BufferedReader(fs);) {

            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

            Map<Integer, List<JsonData>> datasource = mapper.readValue(br.readLine(),
                    new TypeReference<Map<Integer, List<JsonData>>>() {
                    });

            return datasource;

        } catch (Exception e) {

            logger.info("dataSourceファイルの読み込みに失敗", e);

            return new HashMap<>();
        }
    }

}
