package web

import (
	"github.com/npathai/chatter/app"
	"net/http"
)

type Handler struct {
	GetGlobalAppOptions app.AppOptionCreator
	HandlerFunc func(*Context, http.ResponseWriter, *http.Request)
}

func (h Handler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	ctx := &Context{}

	ctx.App = app.New(h.GetGlobalAppOptions()...)

	h.HandlerFunc(ctx, w, r)
}