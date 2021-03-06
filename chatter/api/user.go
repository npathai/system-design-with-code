package api

import (
	"github.com/npathai/chatter/model"
	"github.com/npathai/chatter/web"
	"net/http"
)

func (api *API) InitUser() {
	api.BaseRoutes.Users.Handle("", api.ApiHandler(getUsers)).Methods("GET")
	api.BaseRoutes.Users.Handle("", api.ApiHandler(createUser)).Methods("POST")
	api.BaseRoutes.Users.Handle("/login", api.ApiHandler(login)).Methods("POST")
}

func getUsers(ctx *web.Context, w http.ResponseWriter, r *http.Request) {

	users, err := ctx.App.GetAllUsers()
	if err != nil {
		ctx.Err = err
	}
	w.Write([]byte(model.UserListToJson(users)))
}

func createUser(ctx *web.Context, w http.ResponseWriter, r *http.Request) {
	user := model.UserFromJson(r.Body)
	if user == nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	// Sanitize the input
	// Ask the app layer to create the user from signup
	user, err := ctx.App.CreateUser(user)

	if err != nil {
		ctx.Err = err
		return
	}

	w.WriteHeader(http.StatusCreated)
	w.Write([]byte(user.ToJson()))
}

func login(ctx *web.Context, w http.ResponseWriter, r *http.Request) {
	props := model.MapFromJson(r.Body)
	userId := props["id"]
	loginId := props["loginId"]
	password := props["password"]

	user, err := ctx.App.AuthenticateUserForLogin(userId, loginId, password)
	if err != nil {
		ctx.Err = err
		return
	}

	err = ctx.App.DoLogin(w, r, user)
	if err != nil {
		ctx.Err = err;
		return
	}

	// Sanitize user information to remove sensitive information
	w.Write([]byte(user.ToJson()))
}