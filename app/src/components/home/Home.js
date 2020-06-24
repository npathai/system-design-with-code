import React from 'react'
import Shortener from "../short-url-generator/Shortener";
import RedirectionHistory from "../urls-by-user/RedirectionHistory";
import {AuthContext} from "../../context/AuthContext";

class Home extends React.Component {
    static contextType = AuthContext

    render() {
        const {isLoggedIn} = this.context
        const redirectionHistoryComponent = isLoggedIn
            ? <RedirectionHistory></RedirectionHistory>
            : null
        return (
            <div>
                <Shortener></Shortener>
                {redirectionHistoryComponent}
            </div>
        );
    }
}

export default Home