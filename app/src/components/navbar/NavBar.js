import React from 'react'
import {Link} from 'react-router-dom'
import {connect} from 'react-redux'

class NavBar extends React.Component {

    render() {
        const status = this.props.isLoggedIn ? <h4>{this.props.username} logged in</h4> : null;
        const signIn = this.props.isLoggedIn ? null : <li><Link to="/signin">Sign In</Link></li>

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

export default connect((state, props) => {
    return {
        isLoggedIn: state.auth.isLoggedIn,
        username: state.auth.username
    }
})(NavBar)