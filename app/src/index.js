import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter as Router, Route} from 'react-router-dom'
import {connect} from 'react-redux'

import './index.css';
import Home from "./components/home/Home";
import NavBar from "./components/navbar/NavBar";

import SignIn from "./components/login/SignIn";
import {Provider} from 'react-redux'
import store from './store/store'

class App extends React.Component {
    render() {
        return (
            <div>
                <div>
                    <Router>
                        <NavBar/>
                        <Route exact path="/" component={Home}/>
                        <Route path="/signin" component={SignIn}/>
                    </Router>
                </div>
            </div>
        );
    }
}

ReactDOM.render(
    <Provider store={store}><App/></Provider>, document.getElementById('root')
);
