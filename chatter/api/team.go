package api

import (
	"github.com/npathai/chatter/app"
	"github.com/npathai/chatter/model"
	"github.com/npathai/chatter/web"
	"net/http"
	"strings"
)

func (api *API) InitTeam() {
	api.BaseRoutes.Teams.Handle("", api.ApiSessionRequired(createTeam)).Methods("POST")
}

func createTeam(ctx *web.Context, w http.ResponseWriter, r *http.Request) {
	team := model.TeamFromJson(r.Body)

	team.Email = strings.ToLower(team.Email)

	rTeam, err := ctx.App.createTeamWithUser(team, ctx.App.Session().UserId)
	if err != nil {
		ctx.Err = err
		return
	}
	w.WriteHeader(http.StatusCreated)
	w.Write([]byte(rTeam.toJson()))
}
