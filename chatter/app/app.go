package app

import "github.com/npathai/chatter/model"

type App struct {
	srv *Server
	session model.Session
}

func (app *App) Srv() *Server {
	return app.srv
}

func New(options...AppOption) *App {
	app := &App{}
	for _, option := range options {
		option(app)
	}
	return app
}

func (app *App) SetSession(session *model.Session) {
	app.session = *session
}