package app

type App struct {
	srv *Server
}

func (app *App) Srv() *Server {
	return app.srv
}

func New() *App {
	app := &App{}
	srv, err  := NewServer()
	if err != nil {
		panic("error in creating server")
	}
	app.srv = srv
	return app
}