import React, {useState, createContext, Component} from 'react'

export const AuthContext = createContext({});

class UserProvider extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false,
            user: ""
        }
        this.setLoggedInUser = this.setLoggedInUser.bind(this)
    }

    setLoggedInUser(data) {
        this.setState({
            isLoggedIn: true,
            user: data.username,
            accessToken: data.access_token,
            refreshToken: data.refresh_token,
            tokenType: data.token_type
        })
        const {user, accessToken, refreshToken, tokenType} = this.state
        console.log("Logged in user set in context:" + user)
        console.log("Access token: " + accessToken)
        console.log("Refresh token: " + refreshToken)
        console.log("tokenType: " + tokenType)
    }

    render() {
        const { user } = this.state
        const { setLoggedInUser } = this
        const {isLoggedIn} = this.state
        return (
            <AuthContext.Provider value={{user, setLoggedInUser, isLoggedIn}}>
                {this.props.children}
            </AuthContext.Provider>
        );
    }
}

export default UserProvider