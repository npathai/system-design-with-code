package api

import (
	"encoding/json"
	"fmt"
	"github.com/gorilla/mux"
	"github.com/npathai/chatter/model"
	"io"
	"net/http"
	"strings"
)

func InitTeam(router *mux.Router) {
	fmt.Println("Initializing team api routes")

	sr := router.PathPrefix("/teams").Subrouter()
	sr.HandleFunc("/create_from_signup", createTeamFromSignUp)
}

func createTeamFromSignUp(w http.ResponseWriter, r *http.Request) {
	teamSignup := model.TeamSignupFromJson(r.Body)

	if teamSignup == nil {
		w.WriteHeader(http.StatusBadRequest)
	}

	props := MapFromJson(strings.NewReader(teamSignup.Data))
	teamSignup.Team.Email = props["email"]
	teamSignup.User.Email = props["email"]

	w.WriteHeader(http.StatusOK)
}

func MapFromJson(data io.Reader) map[string]string {
	decoder := json.NewDecoder(data)

	var objmap map[string]string
	if err := decoder.Decode(&objmap); err != nil {
		return make(map[string]string)
	} else {
		return objmap
	}
}
