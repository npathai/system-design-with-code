package api

import (
	"fmt"
	"github.com/gorilla/mux"
	"net/http"
)

func InitTeam(router *mux.Router) {
	fmt.Println("Initializing team api routes")

	sr := router.PathPrefix("/teams").Subrouter()
	sr.HandleFunc("/create_from_signup", createTeamFromSignUp)
}

func createTeamFromSignUp(w http.ResponseWriter, r *http.Request) {

}
