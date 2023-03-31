package br.com.dbc.javamosdecolar.controller;

import br.com.dbc.javamosdecolar.dto.in.AviaoCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.PassagemCreateDTO;
import br.com.dbc.javamosdecolar.dto.in.VooCreateDTO;
import br.com.dbc.javamosdecolar.dto.outs.*;
import br.com.dbc.javamosdecolar.exception.RegraDeNegocioException;
import br.com.dbc.javamosdecolar.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final AdminService adminService;
    private final LogService logService;

    @GetMapping("/buscar-log/{pagina}/{tamanho}")
    public ResponseEntity<PageDTO<LogDTO>> consultLogsUsuario(@RequestParam(value = "idUsuario", required = false) Integer idUsuario,
                                                              @PathVariable("pagina") Integer pagina,
                                                              @PathVariable("tamanho") Integer tamanho) {
        return new ResponseEntity<>(logService.consultLogsUsuario(idUsuario, pagina, tamanho), OK);
    }
    @PostMapping("/create-aviao/{idCompanhia}")
    public ResponseEntity<AviaoDTO> createAviao(@PathVariable("idCompanhia") Integer idCompanhia,
            @Valid @RequestBody AviaoCreateDTO aviaoCreateDTO)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(adminService.createAviao(idCompanhia, aviaoCreateDTO), CREATED);
    }
    @PostMapping("/create-passagem/{idCompanhia}")
    public ResponseEntity<PassagemDTO> createPassagem(@PathVariable("idCompanhia") Integer idCompanhia,
            @RequestBody @Valid PassagemCreateDTO passagemCreateDTO)
            throws RegraDeNegocioException {
        return new ResponseEntity<>(this.adminService.createPassagem(idCompanhia, passagemCreateDTO), CREATED);
    }
    @PostMapping("/create-voo/{idCompanhia}")
    public ResponseEntity<VooDTO> createVoo(@PathVariable("idCompanhia") Integer idCompanhia,
            @RequestBody @Valid VooCreateDTO vooCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(this.adminService.createVoo(idCompanhia, vooCreateDTO), CREATED);
    }
}
