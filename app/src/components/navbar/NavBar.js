import React from 'react'
import {Link} from 'react-router-dom'
import {AuthContext} from "../../context/AuthContext";

class NavBar extends React.Component {
    static contextType = AuthContext

    render() {
        const {user, isLoggedIn} = this.context
        const status = isLoggedIn ? <h4>{user} logged in</h4> : null;
        const signIn = isLoggedIn ? null : <li><Link to="/signin">Sign In</Link></li>

        return (
            <div>
                <ul>
                    <li><Link to="/">Home</Link></li>
                    {status}
                    {signIn}
                </ul>
            </div>
        );
    }
}

export default NavBar