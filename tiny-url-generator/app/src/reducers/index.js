import {combineReducers} from "redux";
import auth from './auth'
import redirection from './redirection'
import error from './error'

export default combineReducers({
    auth: auth,
    redirection: redirection,
    error: error
})