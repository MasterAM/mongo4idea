/*
 * Copyright (c) 2012 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.mongo.view;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.io.IOUtils;
import org.codinjutsu.tools.mongo.model.MongoCollectionResult;
import org.uispec4j.DefaultTreeCellValueConverter;
import org.uispec4j.Panel;
import org.uispec4j.Tree;
import org.uispec4j.UISpecTestCase;

import javax.swing.*;
import java.awt.*;

public class MongoRunnerPanelTest extends UISpecTestCase {


    private MongoRunnerPanel mongoRunnerPanel;
    private Panel uiSpecPanel;


    public void testDisplaySimpleArray() throws Exception {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream("simpleArray.json")));

        MongoCollectionResult mongoCollectionResult = new MongoCollectionResult("mycollec");
        mongoCollectionResult.add(jsonObject);
        mongoRunnerPanel.showResults(mongoCollectionResult);

        Tree tree = uiSpecPanel.getTree();
        tree.setCellValueConverter(new TreeCellConverter());
        tree.contentEquals(
                "results of 'mycollec' #(bold)\n" +
                        "  [0] \"toto\" #(bold)\n" +
                        "  [1] true #(bold)\n" +
                        "  [2] 10 #(bold)\n"
        ).check();
    }



    public void testDisplaySimpleDocument() throws Exception {
        DBObject jsonObject = (DBObject) JSON.parse(IOUtils.toString(getClass().getResourceAsStream("simpleDocument.json")));

        MongoCollectionResult mongoCollectionResult = new MongoCollectionResult("mycollec");
        mongoCollectionResult.add(jsonObject);
        mongoRunnerPanel.showResults(mongoCollectionResult);

        Tree tree = uiSpecPanel.getTree();
        tree.setCellValueConverter(new TreeCellConverter());
        tree.contentEquals(
                "results of 'mycollec' #(bold)\n" +
                        "  \"id\": 0 #(bold)\n" +
                        "  \"label\": \"toto\" #(bold)\n" +
                        "  \"visible\": false #(bold)\n"
        ).check();
    }



    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mongoRunnerPanel = new MongoRunnerPanel();
        uiSpecPanel = new Panel(mongoRunnerPanel);
    }

    private static class TreeCellConverter extends DefaultTreeCellValueConverter {

        @Override
        protected JLabel getLabel(Component renderedComponent) {
            JsonTreeCellRenderer jsonTreeCellRenderer = (JsonTreeCellRenderer) renderedComponent;
            return new JLabel(jsonTreeCellRenderer.toString());
        }
    }
}
