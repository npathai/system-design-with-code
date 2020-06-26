import React from 'react'
import {connect} from 'react-redux'
import Shortener from "../short-url-generator/Shortener";
import RedirectionHistory from "../redirection-history/RedirectionHistory";

class Home extends React.Component {
    render() {
        const isLoggedIn = this.props.isLoggedIn
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

export default connect((state, props) => {
    return {
        isLoggedIn: state.auth.isLoggedIn
    }
})(Home)