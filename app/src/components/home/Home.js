import React from 'react'
import Shortener from "../short-url-generator/Shortener";
import UrlsByUser from "../urls-by-user/UrlsByUser";
import {AuthContext} from "../../context/AuthContext";

class Home extends React.Component {
    static contextType = AuthContext

    render() {
        return (
            <div>
                <Shortener></Shortener>
                <UrlsByUser></UrlsByUser>
            </div>
        );
    }
}

export default Home