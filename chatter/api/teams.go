package api

import (
	"github.com/npathai/chatter/web"
	"net/http"
)

func (api *API) InitTeam() {
	api.BaseRoutes.Teams.Handle("", api.ApiSessionRequired(createTeam)).Methods("POST")
}

func createTeam(context *web.Context, w http.ResponseWriter, r *http.Request) {

}
