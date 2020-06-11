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

    setLoggedInUser(user) {
        this.setState({
            isLoggedIn: true,
            user: user
        })
        console.log("Logged in user set in context: " + user)
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