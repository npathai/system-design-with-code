import React from 'react'
import {connect} from 'react-redux'
import Shortener from "../short-url-generator/Shortener";
import RedirectionHistory from "../redirection-history/RedirectionHistory";

class Home extends React.Component {
    render() {
        let errorMsg = null
        if (this.props.errorMessage) {
            errorMsg = <div className="errorMsg">{this.props.errorMessage}</div>
        }

        const isLoggedIn = this.props.isLoggedIn
        const redirectionHistoryComponent = isLoggedIn
            ? <RedirectionHistory></RedirectionHistory>
            : null

        return (
            <div>
                {errorMsg}
                <Shortener></Shortener>
                {redirectionHistoryComponent}
            </div>
        );
    }
}

export default connect((state, props) => {
    return {
        isLoggedIn: state.auth.isLoggedIn,
        errorMessage: state.error.errorMessage
    }
})(Home)