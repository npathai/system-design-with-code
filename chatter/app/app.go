package app

type App struct {
	srv *Server
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