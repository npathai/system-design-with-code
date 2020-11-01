package app

type App struct {
	srv *Server
}

func (app *App) Srv() *Server {
	return app.srv
}

func New() *App {
	return &App{}
}