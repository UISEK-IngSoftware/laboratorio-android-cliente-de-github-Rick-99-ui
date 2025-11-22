package ec.edu.uisek.githubclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ec.edu.uisek.githubclient.databinding.FragmentRepoitemBinding


class Repoitem : Fragment() {
    private var _binding: FragmentRepoitemBinding?=null
    private val binding get() = _binding!!

    // Método de ciclo de vida llamado al crear el fragmento
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    // Crea y devuelve la jerarquía de vistas asociada con el fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRepoitemBinding.inflate(inflater,container, false)
        return binding.root
    }

    // Llamado inmediatamente después de que onCreateView ha retornado
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.repoName.text = "Mi repositorio"
        binding.repoDescription.text = "Esta la descripción del repositorio"
    }

    // Llamado cuando la vista del fragmento está siendo destruida
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        // Método estático para crear una nueva instancia del fragmento
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Repoitem().apply {
                arguments = Bundle().apply {

                }
            }
    }
}