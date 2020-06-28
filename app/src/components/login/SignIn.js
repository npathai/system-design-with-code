import React from 'react'
import {connect} from "react-redux";
import {Redirect} from 'react-router-dom'
import './SignIn.css'
import * as actions from '../../actions/actions'

class SignIn extends React.Component {

    constructor(props) {
        super(props);
        this.signIn = this.signIn.bind(this)
        this.handleUsernameChange = this.handleUsernameChange.bind(this)
        this.handlePasswordChange = this.handlePasswordChange.bind(this)
    }

    handleUsernameChange(event) {
        this.props.dispatch(actions.changeUsername(event.target.value))
    }

    handlePasswordChange(event) {
        this.props.dispatch(actions.changePassword(event.target.value))
    }

    signIn(e) {
        const credentials = {
            username: this.props.username,
            password: this.props.password
        }
        this.props.dispatch(actions.signIn(credentials));
        e.preventDefault();
    }

    render() {
        let errorMsg = null
        if (this.props.errorMessage) {
            errorMsg = <div className="errorMsg">{this.props.errorMessage}</div>
        }

        const isLoggedIn = this.props.isLoggedIn
        const loginAttemptFailure = this.props.isLoginAttemptFailure

        const loginAttemptFailureFeedback = loginAttemptFailure ?
            <div className="alert alert-error">
                Incorrect Username or Password!
            </div> : null

        return isLoggedIn ? <Redirect to="/"/> :
            <div>
                {errorMsg}
                <form className="form-signin">
                    <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                    <label htmlFor="username" className="sr-only">Username</label>
                    <input type="text" id="username" className="form-control" placeholder="Username"
                           required="" autoFocus=""
                            onChange={this.handleUsernameChange} value={this.props.username}
                    />
                    <label htmlFor="password" className="sr-only">Password</label>
                    <input type="password" id="password" className="form-control" placeholder="Password"
                           required=""
                            onChange={this.handlePasswordChange}
                           value = {this.props.password}
                    />

                    <button className="btn btn-lg btn-primary btn-block" type="button"
                            onClick={this.signIn}>Sign in</button>

                    {loginAttemptFailureFeedback}
                </form>
            </div>
    }
}

export default connect((state, props) => {
    return {
        username: state.auth.username,
        password: state.auth.password,
        isLoggedIn: state.auth.isLoggedIn,
        isLoginAttemptFailure: state.auth.isLoginAttemptFailure,
        errorMessage: state.error.errorMessage
    }
})(SignIn);