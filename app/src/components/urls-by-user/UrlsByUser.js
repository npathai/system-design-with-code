import React from 'react'
import {AuthContext} from "../../context/AuthContext";

class UrlsByUser extends React.Component {
    static contextType = AuthContext

    render() {
        const {isLoggedIn} = this.context
        return isLoggedIn ?
            (
                <div className="row justify-content-center padding">
                    <h3>Urls by user component</h3>
                </div>
            ) : null
    }
}

export default UrlsByUser