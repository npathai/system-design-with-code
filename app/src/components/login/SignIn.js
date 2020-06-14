import React from 'react'
import {Redirect} from 'react-router-dom'
import './SignIn.css'
import {AuthContext} from "../../context/AuthContext";

class SignIn extends React.Component {
    static contextType = AuthContext

    constructor(props) {
        super(props);
        this.state = {
            username: "",
            password: "",
            loginAttemptFailure: false
        }
        this.signIn = this.signIn.bind(this)
        this.handleChange = this.handleChange.bind(this)
    }

    handleChange(e) {
        const {name, value} = e.target
        this.setState({
            [name]: value
        })
    }

    signIn(e) {
        const {setLoggedInUser} = this.context
        fetch("http://localhost:4000/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            mode: 'cors',
            body: JSON.stringify({
                username: this.state.username,
                password: this.state.password
            })
        })
        .then(res => {
            if (res.status !== 200) {
                throw new Error("Invalid login attempt")
            }
            return res.json()
        })
        .then(data => setLoggedInUser(data))
        .catch(err => {
            console.log(err)
            this.setState({
                loginAttemptFailure: true
            })
        })

        e.preventDefault();
    }

    render() {
        const {isLoggedIn} = this.context
        const {loginAttemptFailure} = this.state
        const loginAttemptFailureFeedback = loginAttemptFailure ?
            <div className="alert alert-error">
                Incorrect Username or Password!
            </div> : null

        return isLoggedIn ? <Redirect to="/"/> :
            <div>
                <form className="form-signin">
                    <h1 className="h3 mb-3 font-weight-normal">Please sign in</h1>
                    <label htmlFor="username" className="sr-only">Username</label>
                    <input type="text" id="username" name="username" className="form-control" placeholder="Username"
                           required="" autoFocus=""
                            onChange={this.handleChange}
                    />
                    <label htmlFor="password" className="sr-only">Password</label>
                    <input type="password" id="password" name="password" className="form-control" placeholder="Password"
                           required=""
                            onChange={this.handleChange}
                    />

                    <button className="btn btn-lg btn-primary btn-block" type="button"
                            onClick={this.signIn}>Sign in</button>

                    {loginAttemptFailureFeedback}
                </form>
            </div>
    }
}

export default SignIn