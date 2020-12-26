/*
 * KKuTu-Web (https://github.com/horyu1234/KKuTu-Web)
 * Copyright (C) 2020. horyu1234(admin@horyu.me)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import React, {Component, Suspense} from 'react';
import {BrowserRouter, Route} from 'react-router-dom';
import {Login, Portal} from './pages';
import Loading from "./components/loading/Loading";

class App extends Component {
    render() {
        return (
            <>
                <BrowserRouter>
                    <Suspense fallback={<Loading/>}>
                        <Route exact path="/" component={Portal}/>
                        <Route path="/login" component={Login}/>
                    </Suspense>
                </BrowserRouter>
            </>
        );
    }
}

export default App;