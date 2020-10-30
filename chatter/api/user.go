package api

import (
	"fmt"
	"github.com/npathai/chatter/model"
	"net/http"
)

func (api *API) InitUser() {
	api.BaseRoutes.Users.HandleFunc("", GetUsers).Methods("GET")
	api.BaseRoutes.Users.HandleFunc("", CreateUser).Methods("POST")
}

func GetUsers(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	if _, err := w.Write([]byte("Hello world!")); err != nil {
		fmt.Printf("Error in writing response: %v", err)
	}
}

func CreateUser(w http.ResponseWriter, r *http.Request) {
	user := model.UserFromJson(r.Body)
	if user == nil {
		w.WriteHeader(http.StatusBadRequest)
		return
	}

	// Sanitize the input
	// Ask the app layer to create the user from signup
	w.WriteHeader(http.StatusCreated)
}