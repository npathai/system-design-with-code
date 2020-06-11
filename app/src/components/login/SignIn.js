import React from 'react'
import './SignIn.css'

class SignIn extends React.Component {

    render() {
        return (
            <div>
                <form className="form-signin">
                    <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                    <label htmlFor="username" name="username" className="sr-only">Username</label>
                    <input type="text" id="username" className="form-control" placeholder="Email address"
                           required="" autoFocus=""/>
                    <label htmlFor="password" className="sr-only">Password</label>
                    <input type="password" id="password" name="password" className="form-control" placeholder="Password"
                           required=""/>
                    <button className="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
                </form>
            </div>
        )
    }
}

export default SignIn