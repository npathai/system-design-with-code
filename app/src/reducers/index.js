import {combineReducers} from "redux";
import auth from './auth'
import redirection from './redirection'

export default combineReducers({
    auth: auth,
    redirection: redirection
})