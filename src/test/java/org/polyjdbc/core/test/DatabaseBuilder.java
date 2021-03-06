/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.polyjdbc.core.test;

import org.polyjdbc.core.PolyJDBC;
import org.polyjdbc.core.query.InsertQuery;
import org.polyjdbc.core.query.QueryRunner;

public class DatabaseBuilder {

    private final PolyJDBC polyJDBC;

    private final QueryRunner queryRunner;
    
    private int insertedItems;

    private DatabaseBuilder(PolyJDBC polyJDBC) {
        this.polyJDBC = polyJDBC;
        this.queryRunner = polyJDBC.queryRunner();
    }

    public static DatabaseBuilder database(PolyJDBC polyJDBC) {
        return new DatabaseBuilder(polyJDBC);
    }

    public void buildAndCloseTransaction() {
        queryRunner.commit();
        polyJDBC.close(queryRunner);
    }

    public DatabaseBuilder withItem(String name, int count) {
        return withItem(name, null, count);
    }

    public DatabaseBuilder withItem(String name, String pseudo, int count) {
        InsertQuery insertQuery = polyJDBC.query().insert().into("test").sequence("id", "seq_test")
                .value("name", name).value("pseudo", pseudo)
                .value("some_count", count).value("countable", true)
                .value("separator_char", '|');

        queryRunner.insert(insertQuery);
        return this;
    }

    public DatabaseBuilder withItem(String name) {
        return withItem(name, 42);
    }

    public DatabaseBuilder withItems(int count) {
        for (int i = 0; i < count; ++i) {
            withItem("testItem" + insertedItems);
            insertedItems++;
        }

        return this;
    }
}
