import {applyMiddleware, createStore} from "redux";
import {createLogger} from "redux-logger/src";
import rootReducer from '../reducers/index'
import thunk from "redux-thunk";

const logger = createLogger({
    collapsed: true
})

const store = createStore(
    rootReducer,
    applyMiddleware(thunk, logger)
)

export default store