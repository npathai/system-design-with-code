import React, {createContext} from 'react'

export const AuthContext = createContext({});

class UserProvider extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            isLoggedIn: false,
            user: ""
        }
        this.setLoggedInUser = this.setLoggedInUser.bind(this)
        this.addAuthorizationHeader = this.addAuthorizationHeader.bind(this)
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

    addAuthorizationHeader(headers) {
        const {isLoggedIn, accessToken, tokenType} = this.state
        if (isLoggedIn) {
            headers["Authorization"] = tokenType + " " + accessToken
        }
    }

    render() {
        const { user } = this.state
        const { setLoggedInUser, addAuthorizationHeader } = this
        const {isLoggedIn} = this.state
        return (
            <AuthContext.Provider value={{user, setLoggedInUser, isLoggedIn, addAuthorizationHeader}}>
                {this.props.children}
            </AuthContext.Provider>
        );
    }
}

export default UserProvider