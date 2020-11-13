package web

import (
	"github.com/npathai/chatter/app"
	"net/http"
)

type Handler struct {
	GetGlobalAppOptions app.AppOptionCreator
	HandlerFunc func(*Context, http.ResponseWriter, *http.Request)
	RequireSession bool
}

func (h Handler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	ctx := &Context{}

	ctx.App = app.New(h.GetGlobalAppOptions()...)

	w.Header().Set("Content-Type", "application/json")

	token := app.ParseAuthTokenFromRequest(r)
	if len(token) > 0 {
		session, err := ctx.App.GetSessionByToken(token)

	} else {
		// Not allowed
	}

	h.HandlerFunc(ctx, w, r)
}